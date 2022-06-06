package com.chocomiruku.myhome.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.chocomiruku.myhome.data.repository.AuthRepositoryImpl
import com.chocomiruku.myhome.data.repository.UserRepositoryImpl
import com.chocomiruku.myhome.domain.AuthRepository
import com.chocomiruku.myhome.domain.UserRepository
import com.chocomiruku.myhome.util.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _authStateFlow: MutableStateFlow<AuthState?> =
        MutableStateFlow(null)
    val authStateFlow: StateFlow<AuthState?> = _authStateFlow

    fun onSignIn(email: String, password: String) {
        viewModelScope.launch {
            authRepo.signIn(email, password).collect { authResult ->
                _authStateFlow.value = authResult
            }
        }
    }
}