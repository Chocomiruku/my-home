package com.chocomiruku.myhome.presentation.user_edit

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.databinding.EditProfileFragmentBinding
import com.chocomiruku.myhome.util.generateRandomColor
import dagger.hilt.android.AndroidEntryPoint
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

        bindUserDetails()

        binding.saveBtn.setOnClickListener {
            viewModel.updateUserDetails(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString()
            )

            this.findNavController().navigateUp()
        }

        binding.changeProfilePic.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpg", "image/png"))
            }
            resultLauncher.launch(Intent.createChooser(intent, "Выберите фото"))
        }

        return binding.root
    }

    private fun bindUserDetails() {
        viewModel.userDetailsStateFlow.asLiveData().observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.nameEditText.setText(user.name)
                binding.emailEditText.setText(user.email)

                if (user.profilePicPath != null) {
                    // TODO: Glide
                } else {
                    binding.userProfilePic.setBackgroundColor(generateRandomColor())
                }
            }
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { selectedImagePath ->
                    val inputStream = activity?.contentResolver?.openInputStream(selectedImagePath)
                    val selectedBmp = BitmapFactory.decodeStream(inputStream)
                    val outputStream = ByteArrayOutputStream()
                    selectedBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    selectedImageBytes = outputStream.toByteArray()

                    binding.userProfilePic.setImageBitmap(selectedBmp)
                    viewModel.uploadProfilePic(selectedImageBytes)
                }
            }
        }
}