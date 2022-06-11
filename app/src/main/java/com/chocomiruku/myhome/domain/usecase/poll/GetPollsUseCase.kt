package com.chocomiruku.myhome.domain.usecase.poll

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.domain.repository.PollRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPollsUseCase @Inject constructor(
    private val pollRepo: PollRepository
) {
    operator fun invoke(): Flow<Resource<List<Poll>>> {
        return pollRepo.getPolls()
    }
}