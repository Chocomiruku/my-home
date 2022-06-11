package com.chocomiruku.myhome.domain.usecase.user

import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.repository.UserRepository
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(user: User) {
        return userRepo.addNewUser(user)
    }
}