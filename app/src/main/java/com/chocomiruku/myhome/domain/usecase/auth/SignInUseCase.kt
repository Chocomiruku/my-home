package com.chocomiruku.myhome.domain.usecase.auth

import com.chocomiruku.myhome.domain.repository.AuthRepository
import com.chocomiruku.myhome.util.AuthState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<AuthState> {
        return authRepo.signIn(email, password)
    }
}