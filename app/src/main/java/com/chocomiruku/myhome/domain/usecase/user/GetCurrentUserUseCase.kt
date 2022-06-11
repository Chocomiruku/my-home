package com.chocomiruku.myhome.domain.usecase.user

import com.chocomiruku.myhome.data.Resource
import com.chocomiruku.myhome.domain.models.User
import com.chocomiruku.myhome.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    operator fun invoke(): Flow<Resource<User>> {
        return userRepo.getCurrentUser()
    }
}