package com.chocomiruku.myhome.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.UserRepository
import com.chocomiruku.myhome.domain.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepo: UserRepository
) : ViewModel() {
    private val _userDetailsStateFlow: MutableStateFlow<User?> = MutableStateFlow(null)
    val userDetailsStateFlow: StateFlow<User?> = _userDetailsStateFlow

    init {
        getUserDetails()
    }

    private fun getUserDetails() {
        viewModelScope.launch {
            userRepo.getCurrentUser().collect { user ->
                user?.let {
                    _userDetailsStateFlow.value = user
                }
            }
        }
    }
}