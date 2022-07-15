package com.chocomiruku.myhome.di.usecase

import com.chocomiruku.myhome.domain.repository.UserRepository
import com.chocomiruku.myhome.domain.usecase.user.AddUserUseCase
import com.chocomiruku.myhome.domain.usecase.user.ChangeRoleUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetCurrentUserUseCase
import com.chocomiruku.myhome.domain.usecase.user.GetUserUseCase
import com.chocomiruku.myhome.domain.usecase.user.UpdateCurrentUserUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UserUseCaseModule {
    @Provides
    fun provideAddUserUseCase(
        userRepo: UserRepository
    ): AddUserUseCase {
        return AddUserUseCase(userRepo)
    }

    @Provides
    fun provideGetCurrentUserUseCase(
        userRepo: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(userRepo)
    }

    @Provides
    fun provideGetUserUseCase(
        userRepo: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(userRepo)
    }

    @Provides
    fun provideUpdateCurrentUserUseCase(
        userRepo: UserRepository
    ): UpdateCurrentUserUseCase {
        return UpdateCurrentUserUseCase(userRepo)
    }

    @Provides
    fun provideChangeRoleUseCase(
        userRepo: UserRepository
    ): ChangeRoleUseCase {
        return ChangeRoleUseCase(userRepo)
    }
}