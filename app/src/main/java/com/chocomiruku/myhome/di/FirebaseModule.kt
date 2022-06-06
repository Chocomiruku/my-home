package com.chocomiruku.myhome.di

import android.app.Activity
import com.chocomiruku.myhome.data.repository.AuthRepositoryImpl
import com.chocomiruku.myhome.domain.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthRepo(firebaseAuth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, firestore)
    }

    @Singleton
    @Provides
    fun provideCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
            return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}