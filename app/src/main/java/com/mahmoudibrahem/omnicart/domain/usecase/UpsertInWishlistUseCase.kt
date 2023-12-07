package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UpsertInWishlistUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(productID: String): Flow<Resource<Unit>> {
        return networkRepository.upsertInWishlist(productID = productID)
    }
}