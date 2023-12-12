package com.mahmoudibrahem.omnicart.presentation.screens.user_address

import com.mahmoudibrahem.omnicart.domain.model.UserAddress

data class UserAddressScreenUIState(
    val isLoading: Boolean = true,
    val addressList: List<UserAddress> = emptyList(),
    val selectedAddress: Int = 0
)