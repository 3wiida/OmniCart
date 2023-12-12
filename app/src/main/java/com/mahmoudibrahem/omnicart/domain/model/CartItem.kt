package com.mahmoudibrahem.omnicart.domain.model

data class CartItem(
    val product: String,
    val quantity: Int,
    val id: String,
    val discount: Number?,
    val price: Number,
    val isFav: Boolean,
    val image: String
)
