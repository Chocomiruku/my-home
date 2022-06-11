package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.AuthRepository
import com.chocomiruku.myhome.domain.usecase.auth.GetCurrentUserIdUseCase
import com.chocomiruku.myhome.domain.usecase.auth.SignInUseCase
import com.chocomiruku.myhome.domain.usecase.auth.SignUpUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class AuthUseCaseModule {
    @Provides
    fun provideSignInUseCase(
        authRepo: AuthRepository
    ): SignInUseCase {
        return SignInUseCase(authRepo)
    }

    @Provides
    fun provideSignUpUseCase(
        authRepo: AuthRepository
    ): SignUpUseCase {
        return SignUpUseCase(authRepo)
    }

    @Provides
    fun provideGetCurrentUserIdUseCase(
        auth: FirebaseAuth
    ): GetCurrentUserIdUseCase {
        return GetCurrentUserIdUseCase(auth)
    }
}