package com.mahmoudibrahem.omnicart.presentation.screens.category_products

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class CategoryProductsUIState(
    val isLoading: Boolean = true,
    val products: List<CommonProduct> = emptyList()
)
