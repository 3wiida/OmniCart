package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.HomeResponse

data class HomeResponseDto(
    val categories: List<String>,
    val sales: List<SaleDto>,
    val topSales: List<TopSaleDto>
) {
    data class SaleDto(
        val __v: Int,
        val _id: String,
        val brand: String,
        val category: String,
        val disPercentage: Int,
        val discount: Int,
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
    )

    data class TopSaleDto(
        val __v: Int,
        val _id: String,
        val brand: BrandDto,
        val category: String,
        val disPercentage: Int,
        val discount: Int,
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
        data class BrandDto(
            val _id: String,
            val id: String,
            val name: String
        )
    }

    fun toHomeResponse(): HomeResponse {
        return HomeResponse(
            categories = categories,
            freshSales = sales.map { it.toHomeProduct() },
            topSales = topSales.map { it.toHomeProduct() }
        )
    }

    private fun SaleDto.toHomeProduct(): CommonProduct {
        val image = if (this.images.isNotEmpty()) this.images.first() else ""
        return CommonProduct(
            name = name,
            image = image,
            price = price,
            discount = discount,
            disPercentage = disPercentage
        )
    }

    private fun TopSaleDto.toHomeProduct(): CommonProduct {
        val image = if (this.images.isNotEmpty()) this.images.first() else ""
        return CommonProduct(
            name = name,
            image = image,
            price = price,
            discount = discount,
            disPercentage = disPercentage
        )
    }
}