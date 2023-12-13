package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class WishlistResponseDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val wishlists: List<Wishlists>
    ) {
        data class Wishlists(
            val __v: Int,
            val _id: String,
            val product: Product,
            val user: String
        ) {
            data class Product(
                val _id: String,
                val id: String,
                val images: List<String>,
                val name: String,
                val price: Int,
                val slug: String,
                val disPercentage: Int?,
                val discount: Int?,
                val ratingAverage: Float,
                val ratingQuantity: Int
            )

            fun toCommonProduct(): CommonProduct {
                return CommonProduct(
                    id = product._id,
                    image = if (product.images.isNotEmpty()) product.images.first() else "",
                    name = product.name,
                    price = product.price,
                    disPercentage = product.disPercentage,
                    discount = product.discount,
                    rating = product.ratingAverage
                )
            }
        }
    }
}