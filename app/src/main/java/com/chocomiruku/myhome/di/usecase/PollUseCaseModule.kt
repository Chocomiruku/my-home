package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.PollRepository
import com.chocomiruku.myhome.domain.usecase.poll.AddPolUseCase
import com.chocomiruku.myhome.domain.usecase.poll.ClosePollUseCase
import com.chocomiruku.myhome.domain.usecase.poll.GetPollsUseCase
import com.chocomiruku.myhome.domain.usecase.poll.VoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class PollUseCaseModule {
    @Provides
    fun provideAddPolUseCase(
        pollRepo: PollRepository
    ): AddPolUseCase {
        return AddPolUseCase(pollRepo)
    }

    @Provides
    fun provideClosePollUseCase(
        pollRepo: PollRepository
    ): ClosePollUseCase {
        return ClosePollUseCase(pollRepo)
    }

    @Provides
    fun provideGetPollsUseCase(
        pollRepo: PollRepository
    ): GetPollsUseCase {
        return GetPollsUseCase(pollRepo)
    }

    @Provides
    fun provideVoteUseCase(
        pollRepo: PollRepository
    ): VoteUseCase {
        return VoteUseCase(pollRepo)
    }
}