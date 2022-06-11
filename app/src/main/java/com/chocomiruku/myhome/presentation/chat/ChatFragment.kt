package com.chocomiruku.myhome.presentation.chat

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
import com.chocomiruku.myhome.R
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.databinding.ChatFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private var _binding: ChatFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()

    private lateinit var chatAdapter: MessageAdapter
    private var selectedImageBytes: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChatFragmentBinding.inflate(inflater, container, false)

        bindChat()

        binding.addImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            resultLauncher.launch(intent)
        }

        binding.sendBtn.setOnClickListener {
            if (checkInput()) {
                binding.linearProgressIndicator.isVisible = true
                viewModel.sendMessage(
                    binding.messageEditText.text.toString(),
                    selectedImageBytes
                )

                viewModel.messageSentFlow.asLiveData().observe(viewLifecycleOwner) { messageSent ->
                    if (messageSent) {
                        binding.messageEditText.text?.clear()
                        binding.linearProgressIndicator.isVisible = false
                        selectedImageBytes = null
                    }
                }
            }
        }

        return binding.root
    }

    private fun bindChat() = with(binding) {
        chatAdapter = MessageAdapter(
            currentUserId = viewModel.currentUserId,
        )
        messagesList.adapter = chatAdapter

        viewModel.getMessages()
        viewModel.messagesStateFlow.asLiveData().observe(viewLifecycleOwner) { resource ->
            circularProgressIndicator.isVisible = resource == Resource.Loading
            messagesList.isVisible = resource != Resource.Loading
            sendLayout.isVisible = resource != Resource.Loading

            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { messages ->
                        chatAdapter.submitList(messages)
                        messagesList.scrollToPosition(messages.lastIndex)
                    }
                }
                is Resource.Failure -> {
                    showSnackBar(R.string.unknown_error)
                }
                else -> {}
            }
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    result.data?.data?.let { selectedImagePath ->
                        lifecycleScope.launch {
                            with(binding) {
                                linearProgressIndicator.isVisible = true
                                compressAndSave(selectedImagePath)
                                linearProgressIndicator.isVisible = false
                                sendBtn.isEnabled = true
                                imageInfoText.isVisible = true
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

    private fun checkInput(): Boolean {
        if (binding.messageEditText.text.toString().trim().isEmpty()) {
            showSnackBar(R.string.message_empty)
            return false
        }
        return true
    }

    private fun showSnackBar(stringId: Int) {
        Snackbar.make(
            binding.layout,
            getString(stringId),
            Snackbar.LENGTH_LONG
        )
            .show()
    }

    private companion object {
        const val IMAGE_QUALITY = 50
    }

}