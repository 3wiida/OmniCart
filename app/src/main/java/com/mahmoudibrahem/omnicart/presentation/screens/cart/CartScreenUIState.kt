package com.mahmoudibrahem.omnicart.presentation.screens.cart

import com.mahmoudibrahem.omnicart.domain.model.CartItem

data class CartScreenUIState(
    val isLoading: Boolean = true,
    val cartItems: MutableList<CartItem> = mutableListOf()
)
