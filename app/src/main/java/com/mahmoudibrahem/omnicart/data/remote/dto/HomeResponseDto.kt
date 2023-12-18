package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse

data class HomeResponseDto(
    val categories: List<String>,
    val products: List<RecommendedDto>,
    val sales: List<SaleDto>,
    val topSales: List<TopSaleDto>
) {
    data class RecommendedDto(
        val __v: Int,
        val _id: String,
        val brand: String,
        val category: String,
        val highligth: String,
        val images: List<String>,
        val name: String,
        val overview: String,
        val price: Int,
        val ratingAverage: Float,
        val ratingQuantity: Int,
        val slug: String,
        val updatedAt: String
    )

    data class SaleDto(
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
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val slug: String,
        val updateDisTime: String,
        val updatedAt: String
    )

    data class TopSaleDto(
        val __v: Int,
        val _id: String,
        val brand: Brand,
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
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val slug: String,
        val updateDisTime: String,
        val updatedAt: String
    ) {
        data class Brand(
            val _id: String,
            val id: String,
            val name: String
        )
    }

    fun toHomeResponse(): HomeResponse {
        return HomeResponse(
            categories = categories,
            freshSales = sales.map { it.toHomeProduct() },
            topSales = topSales.map { it.toHomeProduct() },
            recommended = products.map { it.toHomeProduct() }
        )
    }

    private fun SaleDto.toHomeProduct(): CommonProduct {
        val image = if (this.images.isNotEmpty()) this.images.first() else ""
        return CommonProduct(
            name = name,
            image = image,
            price = price,
            discount = discount,
            disPercentage = disPercentage,
            id = _id
        )
    }

    private fun TopSaleDto.toHomeProduct(): CommonProduct {
        val image = if (this.images.isNotEmpty()) this.images.first() else ""
        return CommonProduct(
            name = name,
            image = image,
            price = price,
            discount = discount,
            disPercentage = disPercentage,
            id = _id
        )
    }

    private fun RecommendedDto.toHomeProduct(): CommonProduct {
        val image = if (this.images.isNotEmpty()) this.images.first() else ""
        return CommonProduct(
            name = name,
            image = image,
            price = price,
            discount = null,
            disPercentage = null,
            id = _id
        )
    }
}