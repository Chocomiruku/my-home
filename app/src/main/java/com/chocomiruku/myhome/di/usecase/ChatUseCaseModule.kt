package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.MessageRepository
import com.chocomiruku.myhome.domain.usecase.chat.GetMessagesUseCase
import com.chocomiruku.myhome.domain.usecase.chat.SendMessageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class ChatUseCaseModule {
    @Provides
    fun provideGetMessagesUseCase(
        messageRepo: MessageRepository
    ): GetMessagesUseCase {
        return GetMessagesUseCase(messageRepo)
    }

    @Provides
    fun provideSendMessageUseCase(
        messageRepo: MessageRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(messageRepo)
    }
}