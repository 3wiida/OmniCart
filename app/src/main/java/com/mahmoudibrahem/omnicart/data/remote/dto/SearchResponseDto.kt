package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class SearchResponseDto(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val products: List<Product>
    ) {
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
                val image = if (this.images.isNotEmpty()) this.images.first() else ""
                return CommonProduct(
                    name = this.name,
                    price = this.price,
                    image = image,
                    discount = this.discount,
                    disPercentage = this.disPercentage,
                    id = _id
                )
            }
        }
    }

}