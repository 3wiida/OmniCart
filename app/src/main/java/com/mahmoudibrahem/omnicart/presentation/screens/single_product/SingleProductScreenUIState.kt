package com.mahmoudibrahem.omnicart.presentation.screens.single_product

import com.mahmoudibrahem.omnicart.domain.model.ProductData

data class SingleProductScreenUIState(
    val isLoading: Boolean = true,
    val productData: ProductData = ProductData(),
    val isError: Boolean = false,
    val errorMsg: String? = "",
    val isButtonLoading: Boolean = false,
    val isAddButton: Boolean = true,
    val isInWishlist: Boolean = false,
    val isUpsertInWishlistLoading: Boolean = false
)