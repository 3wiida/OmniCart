package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class OfferResponseDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val product: List<Product>
    ) {
        data class Product(
            val __v: Int,
            val _id: String,
            val brand: String,
            val category: String,
            val disPercentage: Int,
            val discount: Int,
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
        ) {
            fun toCommonProduct(): CommonProduct {
                return CommonProduct(
                    id = _id,
                    name = name,
                    image = if (images.isNotEmpty()) images.first() else "",
                    discount = discount,
                    disPercentage = disPercentage,
                    price = price,
                    rating = ratingAverage
                )
            }
        }

    }
}