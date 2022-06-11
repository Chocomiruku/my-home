package com.chocomiruku.myhome.domain.usecase.poll

import com.chocomiruku.myhome.domain.models.News
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.domain.repository.NewsRepository
import com.chocomiruku.myhome.domain.repository.PollRepository
import javax.inject.Inject

class AddPolUseCase @Inject constructor(
    private val pollRepo: PollRepository
) {
    suspend operator fun invoke(poll: Poll) {
        return pollRepo.addPoll(poll)
    }
}