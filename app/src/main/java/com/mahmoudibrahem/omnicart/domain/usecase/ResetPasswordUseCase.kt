package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(token: String, password: String, confirmPassword: String) =
        networkRepository.resetPassword(
            token = token,
            password = password,
            confirmPassword = confirmPassword
        )
}