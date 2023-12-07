package com.mahmoudibrahem.omnicart.domain.model

data class SingleProductInfo(
    val id: String = "",
    val name: String = "",
    val price: Int = 0,
    val discount: Number? = null,
    val images: List<String> = emptyList(),
    val highlight: String = "",
    val overview: String = "",
    val ratingAverage: Double = 0.0,
    val ratingsCount: Int = 0,
    val category: String = "",
    val disPercentage: Int = 0,
    val isInFavorites: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val isInCart: Boolean = false
)
