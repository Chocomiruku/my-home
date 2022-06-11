package com.chocomiruku.myhome.domain.repository

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Poll
import kotlinx.coroutines.flow.Flow

interface PollRepository {
    suspend fun addPoll(poll: Poll)
    suspend fun closePoll(pollId: String)
    suspend fun vote(pollId: String, selectedOptions: List<String>)
    fun getPolls(): Flow<Resource<List<Poll>>>
}