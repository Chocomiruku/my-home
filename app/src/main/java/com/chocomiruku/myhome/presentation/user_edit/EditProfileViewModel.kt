package com.chocomiruku.myhome.presentation.user_edit

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
class EditProfileViewModel @Inject constructor(
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

    fun updateUserDetails(name: String, email: String) {
        viewModelScope.launch {
            userRepo.updateCurrentUser(User(name, email))
        }
    }

    fun uploadProfilePic(picBytes: ByteArray?) {
        picBytes?.let {
            viewModelScope.launch {
                userRepo.uploadProfilePic(picBytes).collect { ref ->
                    ref?.let {
                        userRepo.updateCurrentUser(User("", "", ref))
                    }
                }
            }
        }
    }
}