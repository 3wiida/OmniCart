package com.mahmoudibrahem.omnicart.domain.model

data class SingleOrder(
    val id:String,
    val orderStatus: String,
    val products: List<CartItem>,
    val orderDate: String,
    val address: UserAddress,
    val resitID:String
)
