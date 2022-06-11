package com.chocomiruku.myhome.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Message
import com.chocomiruku.myhome.domain.usecase.auth.GetCurrentUserIdUseCase
import com.chocomiruku.myhome.domain.usecase.chat.GetMessagesUseCase
import com.chocomiruku.myhome.domain.usecase.chat.SendMessageUseCase
import com.chocomiruku.myhome.domain.usecase.images.UploadImageUseCase
import com.chocomiruku.myhome.util.ImageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {

    val currentUserId = getCurrentUserIdUseCase.invoke()

    private val _messagesStateFlow: MutableStateFlow<Resource<List<Message>>> =
        MutableStateFlow(Resource.Loading)
    val messagesStateFlow: StateFlow<Resource<List<Message>>> = _messagesStateFlow

    private val _messageSentFlow: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val messageSentFlow: StateFlow<Boolean> = _messageSentFlow

    fun getMessages() {
        viewModelScope.launch {
            getMessagesUseCase.invoke()
                .collect { resource ->
                    _messagesStateFlow.value = resource
                }
        }
    }

    fun sendMessage(text: String, imageBytes: ByteArray?) {
        viewModelScope.launch {
            if (imageBytes != null) {
                uploadImageUseCase.invoke(imageBytes, TYPE).collect { imageUri ->
                    imageUri?.let {
                        sendMessageUseCase.invoke(
                            Message(
                                messageId = "",
                                text = text,
                                date = System.currentTimeMillis(),
                                imageUri = imageUri,
                                userId = currentUserId
                            )
                        )
                    }
                    _messageSentFlow.value = true
                }
            } else {
                sendMessageUseCase.invoke(
                    Message(
                        messageId = "",
                        text = text,
                        date = System.currentTimeMillis(),
                        imageUri = null,
                        userId = currentUserId
                    )
                )
                _messageSentFlow.value = true
            }
        }
    }

    private companion object {
        val TYPE = ImageType.MESSAGE
    }
}