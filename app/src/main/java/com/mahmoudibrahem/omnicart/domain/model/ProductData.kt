package com.mahmoudibrahem.omnicart.domain.model

data class ProductData(
    val productInfo: SingleProductInfo = SingleProductInfo(),
    val recommendedProducts: List<CommonProduct> = emptyList(),
)
