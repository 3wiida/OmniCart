package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChangeQuantityUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(productID: String, isIncrease: Boolean): Flow<Resource<Unit>> {
        return if (isIncrease) {
            networkRepository.addToCart(productID = productID)
        } else {
            networkRepository.decreaseQuantityOfItem(productID = productID)
        }
    }
}