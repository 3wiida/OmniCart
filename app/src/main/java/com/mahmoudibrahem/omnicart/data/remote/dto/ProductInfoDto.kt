package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.model.ProductData
import com.mahmoudibrahem.omnicart.domain.model.Review
import com.mahmoudibrahem.omnicart.domain.model.SingleProductInfo

data class ProductInfoDto(
    val product: Product,
    val recommended: List<Recommended>,
    val status: String
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
        val isFav: Boolean,
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val reviews: List<ReviewDto>,
        val slug: String,
        val inCart:Boolean,
        val updateDisTime: String,
        val updatedAt: String
    ) {
        data class ReviewDto(
            val __v: Int,
            val _id: String,
            val createdAt: String,
            val id: String,
            val product: String,
            val rating: Float,
            val review: String,
            val updatedAt: String,
            val user: UserDto
        ) {
            data class UserDto(
                val _id: String,
                val id: String,
                val name: String
            ) {
                fun toUser(): Pair<String, String> {
                    return Pair(id, name)
                }
            }

            fun toReview(): Review {
                return Review(
                    userId = user.toUser().first,
                    username = user.toUser().second,
                    rating = rating,
                    review = review,
                    time = createdAt
                )
            }
        }

        fun toSingleProductInfo(): SingleProductInfo {
            return SingleProductInfo(
                id = _id,
                name = name,
                price = price,
                discount = discount,
                disPercentage = disPercentage,
                images = images,
                highlight = highligth,
                overview = overview,
                ratingAverage = ratingAverage,
                ratingsCount = ratingQuantity,
                category = category,
                reviews = reviews.map { it.toReview() },
                isInCart = inCart,
                isInFavorites = isFav
            )
        }
    }


    data class Recommended(
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
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val slug: String,
        val updateDisTime: String,
        val updatedAt: String
    ) {
        fun toCommonProduct(): CommonProduct {
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
    }

    fun toProductData(): ProductData {
        return ProductData(
            productInfo = product.toSingleProductInfo(),
            recommendedProducts = recommended.map { it.toCommonProduct() }
        )
    }

}