package com.chocomiruku.myhome.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.usecase.auth.GetCurrentUserIdUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val _userDetailsStateFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Loading)
    val userDetailsStateFlow: StateFlow<Resource<User>> = _userDetailsStateFlow

    val currentUserId = getCurrentUserIdUseCase.invoke()

    fun getUserDetails(userId: String?) {
        if (userId == null) {
            viewModelScope.launch {
                getCurrentUserUseCase.invoke().collect { resource ->
                    _userDetailsStateFlow.value = resource
                }
            }
        } else {
            viewModelScope.launch {
                getUserUseCase.invoke(userId).collect { resource ->
                    _userDetailsStateFlow.value = resource
                }
            }
        }
    }
}