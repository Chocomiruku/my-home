package com.chocomiruku.myhome.domain.usecase.user

import com.chocomiruku.myhome.domain.repository.UserRepository
import javax.inject.Inject

class UpdateCurrentUserUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        imageUri: String?,
        notifications: Boolean
    ) {
        return userRepo.updateCurrentUser(name, email, imageUri, notifications)
    }
}