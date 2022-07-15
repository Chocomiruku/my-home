package com.chocomiruku.myhome.domain.usecase.user

import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.repository.UserRepository
import com.chocomiruku.myhome.util.UserRole
import javax.inject.Inject

class ChangeRoleUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(
        user: User
    ) {
        return userRepo.changeRole(user)
    }
}