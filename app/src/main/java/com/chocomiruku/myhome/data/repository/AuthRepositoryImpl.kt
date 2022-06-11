package com.chocomiruku.myhome.data.repository

import android.util.Log
import com.chocomiruku.myhome.domain.repository.AuthRepository
import com.chocomiruku.myhome.util.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override fun signIn(email: String, password: String): Flow<AuthState> = flow {
        try {
            emit(AuthState.LOADING)
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "signInWithEmail:success")
            emit(AuthState.SIGN_IN_SUCCESS)
        } catch (e: Exception) {
            Log.d(TAG, "signInWithEmail:failure", e)
            emit(AuthState.SIGN_IN_ERROR)
        }
    }

    override fun signUp(email: String, password: String, contractNumber: String): Flow<AuthState> = flow {
        try {
            emit(AuthState.LOADING)
            checkContract(contractNumber)
                .flowOn(Dispatchers.IO)
                .collect { exist ->
                    if (exist) {
                        auth.createUserWithEmailAndPassword(email, password).await()
                        Log.d(TAG, "createUserWithEmail:success")
                        emit(AuthState.SIGN_UP_SUCCESS)
                    } else emit(AuthState.SIGN_UP_CONTRACT_ERROR)
                }
        } catch (e: Exception) {
            Log.d(TAG, "createUserWithEmail:failure", e)
            emit(AuthState.SIGN_UP_ERROR)
        }
    }

    override fun signOut() {
        auth.signOut()
    }

    private fun checkContract(contractNumber: String): Flow<Boolean> = flow {
        val documentSnapshot = firestore.document("contracts/${contractNumber}").get().await()
        emit(documentSnapshot.exists())
    }

    private companion object {
        const val TAG = "FirebaseAuth"
    }
}