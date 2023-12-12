package com.mahmoudibrahem.omnicart.presentation.screens.add_address

data class AddAddressScreenUIState(
    val addressName: String = "",
    val country: String = "",
    val city: String = "",
    val addressLine1: String = "",
    val addressLine2: String = "",
    val zipCode: String = "",
    val phoneNumber: String = "",
    val isAddButtonLoading: Boolean = false,
    val isAdditionSuccess: Boolean = false
)