package com.chocomiruku.myhome.domain.usecase.poll

import com.chocomiruku.myhome.domain.repository.PollRepository
import javax.inject.Inject

class ClosePollUseCase @Inject constructor(
    private val pollRepo: PollRepository
) {
    suspend operator fun invoke(pollId: String) {
        return pollRepo.closePoll(pollId)
    }
}