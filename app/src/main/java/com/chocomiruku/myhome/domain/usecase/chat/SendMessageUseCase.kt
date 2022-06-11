package com.chocomiruku.myhome.domain.usecase.chat

import com.chocomiruku.myhome.domain.models.Message
import com.chocomiruku.myhome.domain.repository.MessageRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messageRepo: MessageRepository
) {
    suspend operator fun invoke(message: Message) {
        return messageRepo.sendMessage(message)
    }
}