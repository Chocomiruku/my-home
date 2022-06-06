package com.chocomiruku.myhome.domain

import com.chocomiruku.myhome.util.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signIn(email: String, password: String) : Flow<AuthState>
    fun signUp(email: String, password: String, contractNumber: String) : Flow<AuthState>
    fun signOut()
}