package com.mahmoudibrahem.omnicart.domain.model

data class Order(
    val id: String,
    val displayID: String,
    val productsCount: Int,
    val totalPrice: Int,
    val orderStatus: String,
    val orderDate: String,
)
