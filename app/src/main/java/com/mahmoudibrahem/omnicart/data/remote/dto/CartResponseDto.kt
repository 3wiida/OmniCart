package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CartItem

data class CartResponseDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val cart: Cart
    ) {
        data class Cart(
            val items: List<Item>
        ) {
            data class Item(
                val _id: String,
                val id: String,
                val owner: Owner,
                val product: Product,
                val quantity: Int
            ) {
                data class Owner(
                    val __v: Int,
                    val _id: String,
                    val active: Boolean,
                    val email: String,
                    val id: String,
                    val name: String,
                    val role: String
                )

                data class Product(
                    val __v: Int,
                    val _id: String,
                    val brand: String,
                    val category: String,
                    val disPercentage: Int,
                    val discount: Int?,
                    val highligth: String,
                    val id: String,
                    val images: List<String>,
                    val inCart: Boolean,
                    val isFav: Boolean,
                    val name: String,
                    val overview: String,
                    val price: Int,
                    val ratingAverage: Float,
                    val ratingQuantity: Int,
                    val slug: String,
                    val updateDisTime: String,
                    val updatedAt: String
                )

                fun toCartItem(): CartItem {
                    return CartItem(
                        product = this.product.name,
                        quantity = this.quantity,
                        id = this.product._id,
                        discount = this.product.discount,
                        price = this.product.price,
                        isFav = this.product.isFav,
                        image = if (this.product.images.isNotEmpty()) this.product.images.first() else ""
                    )
                }
            }
        }
    }
}