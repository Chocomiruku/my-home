package com.chocomiruku.myhome.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.usecase.auth.GetCurrentUserIdUseCase
import com.chocomiruku.myhome.domain.usecase.user.ChangeRoleUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetUserUseCase
import com.chocomiruku.myhome.util.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val changeRoleUseCase: ChangeRoleUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase
) : ViewModel() {
    private val _otherUserStateFlow: MutableStateFlow<Resource<User>> =
        MutableStateFlow(Resource.Loading)
    val userDetailsStateFlow: StateFlow<Resource<User>> = _otherUserStateFlow

    private val _currentRoleStateFlow: MutableStateFlow<UserRole?> =
        MutableStateFlow(null)
    val currentRoleStateFlow: StateFlow<UserRole?> = _currentRoleStateFlow

    private val _roleChanged: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)
    val roleChanged: StateFlow<Boolean?> = _roleChanged

    val currentUserId = getCurrentUserIdUseCase.invoke()

    init {
        viewModelScope.launch {
            getCurrentUserUseCase.invoke().collect { resource ->
                _currentRoleStateFlow.value = (resource as Resource.Success).data?.role
            }
        }
    }

    fun getUserDetails(userId: String?) {
        if (userId == null) {
            viewModelScope.launch {
                getCurrentUserUseCase.invoke().collect { resource ->
                    _otherUserStateFlow.value = resource
                }
            }
        } else {
            viewModelScope.launch {
                getUserUseCase.invoke(userId).collect { resource ->
                    _otherUserStateFlow.value = resource
                }
            }
        }
    }

    fun changeRole() {
        val user = (userDetailsStateFlow.value as Resource.Success).data
        user?.let {
            viewModelScope.launch {
                changeRoleUseCase.invoke(user)
            }
            _roleChanged.value = true
        }
    }
}