package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.LoginResponse
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<LoginResponse>> {
        return networkRepository.login(email = email, password = password)
    }
}