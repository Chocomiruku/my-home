package com.chocomiruku.myhome.domain.usecase.poll

import com.chocomiruku.myhome.domain.repository.PollRepository
import javax.inject.Inject

class VoteUseCase @Inject constructor(
    private val pollRepo: PollRepository
) {
    suspend operator fun invoke(pollId: String, selectedOptions: List<String>) {
        return pollRepo.vote(pollId, selectedOptions)
    }
}