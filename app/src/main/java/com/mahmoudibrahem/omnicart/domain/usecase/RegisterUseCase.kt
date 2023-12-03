package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Flow<Resource<RegisterResponse>> {
        return networkRepository.register(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword
        )
    }
}