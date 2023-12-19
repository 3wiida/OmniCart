package com.mahmoudibrahem.omnicart.presentation.screens.offer

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class OfferScreenUIState(
    val isLoading: Boolean = true,
    val products: List<CommonProduct> = emptyList()
)