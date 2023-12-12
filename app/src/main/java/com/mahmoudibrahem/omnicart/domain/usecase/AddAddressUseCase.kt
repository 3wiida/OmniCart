package com.mahmoudibrahem.omnicart.domain.usecase

import com.mahmoudibrahem.omnicart.domain.repository.NetworkRepository
import javax.inject.Inject

class AddAddressUseCase @Inject constructor(
    private val networkRepository: NetworkRepository
) {
    suspend operator fun invoke(
        addressName: String,
        country: String,
        city: String,
        addressLine1: String,
        addressLine2: String,
        zipCode: String,
        phoneNumber: String
    ) = networkRepository.addAddress(
        addressName = addressName,
        country = country,
        city = city,
        addressLine1 = addressLine1,
        addressLine2 = addressLine2,
        zipCode = zipCode,
        phoneNumber = phoneNumber
    )
}