package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CartActionResponse
import com.mahmoudibrahem.omnicart.domain.model.CartItem

data class CartActionResponseDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val cart: List<Cart>
    ) {
        data class Cart(
            val _id: String,
            val id: String,
            val owner: String,
            val product: String,
            val quantity: Int
        ) {
            fun toCartItem(): CartItem {
                return CartItem(
                    id = id,
                    product = product,
                    quantity = quantity
                )
            }
        }
    }

    fun toCartActionResponse(): CartActionResponse {
        return CartActionResponse(cart = data.cart.map { it.toCartItem() })
    }
}