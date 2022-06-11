package com.chocomiruku.myhome.domain.usecase.chat

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Message
import com.chocomiruku.myhome.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMessagesUseCase @Inject constructor(
    private val messageRepo: MessageRepository
) {
    operator fun invoke(): Flow<Resource<List<Message>>> {
        return messageRepo.getMessages()
    }
}