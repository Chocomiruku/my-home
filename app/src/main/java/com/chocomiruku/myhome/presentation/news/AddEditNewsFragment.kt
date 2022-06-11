package com.chocomiruku.myhome.presentation.news

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.databinding.AddEditNewsFragmentBinding
import com.chocomiruku.myhome.domain.models.News
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class AddEditNewsFragment : Fragment() {

    private var _binding: AddEditNewsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AddEditNewsViewModel by viewModels()

    private var currentNews: News? = null
    private var selectedImageBytes: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEditNewsFragmentBinding.inflate(inflater, container, false)

        val news = AddEditNewsFragmentArgs.fromBundle(requireArguments()).news
        news?.let {
            currentNews = news
            bindNewsInfo(it)
        }

        binding.publishBtn.setOnClickListener {
            binding.progressIndicator.show()
            if (checkInput()) {
                if (news == null) {
                    viewModel.addNews(
                        binding.titleEditText.text.toString(),
                        binding.newsEditText.text.toString(),
                        selectedImageBytes
                    )
                } else {
                    viewModel.updateNews(
                        news.newsId,
                        binding.titleEditText.text.toString(),
                        binding.newsEditText.text.toString(),
                        selectedImageBytes
                    )
                }

                viewModel.navigateUpFlow.asLiveData().observe(viewLifecycleOwner) { navigateUp ->
                    if (navigateUp) {
                        binding.progressIndicator.hide()
                        this.findNavController().navigateUp()
                        news?.let {
                            showSnackBar(R.string.changes_saved)
                        }
                    }
                }
            }
        }

        binding.addImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            resultLauncher.launch(intent)
        }

        return binding.root
    }

    private fun bindNewsInfo(news: News) = with(binding) {
        publishBtn.text = getString(R.string.save)
        titleEditText.setText(news.title)
        newsEditText.setText(news.text)
        imageInfoText.isVisible = news.imageUri != null
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    result.data?.data?.let { selectedImagePath ->
                        lifecycleScope.launch {
                            with(binding) {
                                progressIndicator.show()
                                publishBtn.isEnabled = false
                                compressAndSave(selectedImagePath)
                                progressIndicator.hide()
                                publishBtn.isEnabled = true
                                imageInfoText.isVisible = true
                                currentNews?.let {
                                    imageInfoText.text = getString(R.string.image_changed)
                                }
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
            }
        }

    private fun checkInput(): Boolean = with(binding) {
        if (titleEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.title_empty)
            return false
        }
        if (newsEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.enter_text)
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
        const val IMAGE_QUALITY = 90
        const val SNACKBAR_MAX_LINES = 5
    }
}