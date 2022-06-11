package com.chocomiruku.myhome.presentation.polls_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.Poll
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.usecase.poll.ClosePollUseCase
import com.chocomiruku.myhome.domain.usecase.poll.GetPollsUseCase
import com.chocomiruku.myhome.domain.usecase.poll.VoteUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PollsViewModel @Inject constructor(
    private val getPollsUseCase: GetPollsUseCase,
    private val voteUseCase: VoteUseCase,
    private val closePollUseCase: ClosePollUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    private val _userDetailsStateFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Loading)
    val userDetailsStateFlow: StateFlow<Resource<User>> = _userDetailsStateFlow

    private val _pollsStateFlow: MutableStateFlow<Resource<List<Poll>>> =
        MutableStateFlow(Resource.Loading)
    val pollsStateFlow: StateFlow<Resource<List<Poll>>> = _pollsStateFlow

    fun getUserDetails() {
        viewModelScope.launch {
            getCurrentUserUseCase.invoke().collect { resource ->
                _userDetailsStateFlow.value = resource
            }
        }
    }

    fun getPolls() {
        viewModelScope.launch {
            getPollsUseCase.invoke().collect { resource ->
                _pollsStateFlow.value = resource
            }
        }
    }

    fun vote(pollId: String, selectedOptions: List<String>) {
        viewModelScope.launch {
            voteUseCase.invoke(pollId, selectedOptions)
        }
    }

    fun close(pollId: String) {
        viewModelScope.launch {
            closePollUseCase.invoke(pollId)
        }
    }
}