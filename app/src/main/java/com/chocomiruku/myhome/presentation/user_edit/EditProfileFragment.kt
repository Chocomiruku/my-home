package com.chocomiruku.myhome.presentation.user_edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.EditProfileFragmentBinding
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.util.isEmailValid
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private var _binding: EditProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditProfileViewModel by viewModels()

    private var selectedImageBytes: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditProfileFragmentBinding.inflate(inflater, container, false)

        val user = EditProfileFragmentArgs.fromBundle(requireArguments()).user
        bindUserDetails(user)

        binding.saveBtn.setOnClickListener {
            if (checkInput()) {
                binding.progressIndicator.show()
                viewModel.updateUserDetails(
                    binding.nameEditText.text.toString(),
                    binding.emailEditText.text.toString(),
                    selectedImageBytes,
                    binding.notificationsSwitch.isChecked
                )

                viewModel.navigateUpFlow.asLiveData().observe(viewLifecycleOwner) { navigateUp ->
                    if (navigateUp) {
                        showSnackBar(R.string.changes_saved)
                        binding.progressIndicator.hide()
                        binding.root.findNavController().navigateUp()
                    }
                }
            }
        }

        binding.changeProfilePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            resultLauncher.launch(intent)
        }

        return binding.root
    }

    private fun bindUserDetails(user: User) = with(binding) {
        nameEditText.setText(user.name)
        emailEditText.setText(user.email)
        contractEditText.setText(user.contractNumber)

        defaultImageText.isVisible = user.imageUri == null
        notificationsSwitch.isChecked = user.notifications

        if (user.imageUri != null) {
            loadImage(user.imageUri)
        } else {
            profileImage.setBackgroundColor(user.defaultColorId)
            defaultImageText.text = user.name.first().uppercase()
        }
    }

    private fun loadImage(uriString: String) = with(binding) {
        val uri = uriString.toUri()

        Glide.with(profileImage.context)
            .load(uri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_anim)
                    .error(R.drawable.ic_broken_image)
            )
            .into(profileImage)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    result.data?.data?.let { selectedImagePath ->
                        lifecycleScope.launch {
                            with(binding) {
                                progressIndicator.show()
                                compressAndSave(selectedImagePath)
                                progressIndicator.hide()
                            }
                        }
                    }
                } catch (e: Exception) {
                    showSnackBar(R.string.image_upload_error)
                }
            }
        }

    private suspend fun compressAndSave(path: Uri) =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                activity?.contentResolver?.openInputStream(path)
            }.onSuccess { inputStream ->
                val selectedBmp = BitmapFactory.decodeStream(inputStream)
                val outputStream = ByteArrayOutputStream()
                selectedBmp.compress(Bitmap.CompressFormat.PNG, IMAGE_QUALITY, outputStream)
                selectedImageBytes = outputStream.toByteArray()

                withContext(Dispatchers.Main) {
                    with(binding) {
                        Glide.with(profileImage.context)
                            .load(selectedImageBytes)
                            .apply(
                                RequestOptions()
                                    .placeholder(R.drawable.loading_anim)
                                    .error(R.drawable.ic_broken_image)
                            )
                            .into(profileImage)
                        defaultImageText.isVisible = false
                    }
                }
            }
        }

    private fun checkInput(): Boolean = with(binding) {
        if (nameEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_name)
            return false
        }
        if (emailEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_email)
            return false
        }
        if (!isEmailValid(emailEditText.text.toString().trim())) {
            showSnackBar(R.string.email_invalid)
            return false
        }
        return true
    }

    private fun showSnackBar(stringId: Int) {
        val snackBar = Snackbar.make(
            binding.layout,
            getString(stringId),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(R.string.action_ok) {
        }
        snackBar.setTextMaxLines(SNACKBAR_MAX_LINES)
        snackBar.show()
    }

    private companion object {
        const val IMAGE_QUALITY = 70
        const val SNACKBAR_MAX_LINES = 6
    }
}