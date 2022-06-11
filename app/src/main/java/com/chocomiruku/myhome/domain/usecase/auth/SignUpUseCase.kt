package com.chocomiruku.myhome.domain.usecase.auth

import com.chocomiruku.myhome.domain.repository.AuthRepository
import com.chocomiruku.myhome.util.AuthState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepo: AuthRepository
) {
    operator fun invoke(email: String, password: String, contractNumber: String): Flow<AuthState> {
        return authRepo.signUp(email, password, contractNumber)
    }
}