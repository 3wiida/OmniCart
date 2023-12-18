package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.Order

data class OrdersResponseDto(
    val orders: List<OrderDto>,
    val totalQuantity: Int
) {
    data class OrderDto(
        val __v: Int,
        val _id: String,
        val createdAt: String,
        val id: String,
        val orderAt: String,
        val orderStatus: String,
        val products: List<ProductDto>,
        val totalPrice: Int,
        val uId: String,
        val updatedAt: String,
        val user: String
    ) {
        data class ProductDto(
            val _id: String,
            val id: String,
            val owner: String,
            val product: String,
            val quantity: Int
        )

        fun toOrder(): Order {
            return Order(
                id = id,
                displayID = uId,
                productsCount = products.size,
                totalPrice = totalPrice,
                orderStatus = orderStatus,
                orderDate = createdAt
            )
        }

    }
}