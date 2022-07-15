package com.chocomiruku.myhome.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.usecase.auth.SignUpUseCase
import com.chocomiruku.myhome.domain.usecase.user.AddUserUseCase
import com.chocomiruku.myhome.util.AuthState
import com.chocomiruku.myhome.util.generateRandomColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val addUserUseCase: AddUserUseCase
) : ViewModel() {

    private val _authStateFlow: MutableStateFlow<AuthState?> =
        MutableStateFlow(null)
    val authStateFlow: StateFlow<AuthState?> = _authStateFlow

    fun onSignUp(name: String, contractNumber: String, email: String, password: String) {
        viewModelScope.launch {
            signUpUseCase.invoke(email, password, contractNumber).collect { authResult ->
                if (authResult == AuthState.SIGN_UP_SUCCESS) {
                    addUserUseCase.invoke(
                        User(
                            uid = "",
                            name = name,
                            email = email,
                            defaultColorId = generateRandomColor(),
                            contractNumber = contractNumber,
                            notifications = true,
                            userRole = "default"
                        )
                    )
                }

                _authStateFlow.value = authResult
            }
        }
    }
}