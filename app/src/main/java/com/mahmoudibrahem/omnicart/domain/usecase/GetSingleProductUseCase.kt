package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.core.util.Resource
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.SingleProductInfo
import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSingleProductUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(productID: String): Flow<Resource<ProductData>> {
        return networkRepository.getSingleProduct(id = productID)
    }
}