package com.mahmoudibrahem.omnicart.presentation.screens.single_order

import com.mahmoudibrahem.omnicart.domain.model.SingleOrder

data class SingleOrderScreenUIState(
    val isLoading: Boolean = true,
    val order: SingleOrder? = null
)
