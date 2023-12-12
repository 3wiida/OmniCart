package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<CartItem>>> {
        return networkRepository.getMyCart()
    }
}