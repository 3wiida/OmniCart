package com.mahmoudibrahem.omnicart.presentation.screens.orders

import com.mahmoudibrahem.omnicart.domain.model.Order

data class OrdersScreenUIState(
    val isLoading: Boolean = true,
    val orders: List<Order> = emptyList()
)