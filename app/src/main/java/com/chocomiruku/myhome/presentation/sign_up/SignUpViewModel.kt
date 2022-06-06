package com.chocomiruku.myhome.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.AuthRepository
import com.chocomiruku.myhome.domain.UserRepository
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.util.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepo: AuthRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _authStateFlow: MutableStateFlow<AuthState?> =
        MutableStateFlow(null)
    val authStateFlow: StateFlow<AuthState?> = _authStateFlow

    fun onSignUp(name: String, contractNumber: String, email: String, password: String) {
        viewModelScope.launch {
            authRepo.signUp(email, password, contractNumber).collect { authResult ->
                if (authResult == AuthState.SIGN_UP_SUCCESS) {
                    userRepo.addNewUser(User(name, email, contractNumber))
                }

                _authStateFlow.value = authResult
            }
        }
    }
}