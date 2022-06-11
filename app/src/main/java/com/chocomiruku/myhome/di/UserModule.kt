package com.chocomiruku.myhome.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UserModule {
    @Provides
    fun provideCurrentUser(auth: FirebaseAuth): FirebaseUser? {
        return auth.currentUser
    }
}