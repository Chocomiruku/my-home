package com.chocomiruku.myhome.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.usecase.auth.SignInUseCase
import com.chocomiruku.myhome.util.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _authStateFlow: MutableStateFlow<AuthState?> =
        MutableStateFlow(null)
    val authStateFlow: StateFlow<AuthState?> = _authStateFlow

    fun onSignIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase.invoke(email, password).collect { authResult ->
                _authStateFlow.value = authResult
            }
        }
    }
}