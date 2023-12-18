package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import javax.inject.Inject

class CompleteOrderUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke() = networkRepository.completeOrder()
}