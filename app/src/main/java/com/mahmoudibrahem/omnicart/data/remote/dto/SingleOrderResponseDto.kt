package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.CartItem
import com.mahmoudibrahem.omnicart.domain.model.SingleOrder
import com.mahmoudibrahem.omnicart.domain.model.UserAddress

data class SingleOrderResponseDto(
    val order: Order,
    val status: String,
    val totalItems: Int
) {
    data class Order(
        val __v: Int,
        val _id: String,
        val createdAt: String,
        val id: String,
        val orderAt: String,
        val orderStatus: String,
        val products: List<Product>,
        val totalPrice: Int,
        val uId: String,
        val updatedAt: String,
        val user: User
    ) {
        data class Product(
            val _id: String,
            val id: String,
            val owner: String,
            val product: Product,
            val quantity: Int
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

            fun toCartProduct(): CartItem {
                return CartItem(
                    product = product.name,
                    quantity = quantity,
                    id = product.id,
                    discount = product.discount,
                    price = product.price,
                    isFav = product.isFav,
                    image = if (product.images.isNotEmpty()) product.images.first() else ""
                )
            }
        }

        data class User(
            val _id: String,
            val email: String,
            val id: String,
            val location: List<Location>,
            val name: String
        ) {
            data class Location(
                val __v: Int,
                val _id: String,
                val city: String,
                val country: String,
                val id: String,
                val name: String,
                val phoneNumber: String,
                val street: String,
                val street2: String,
                val user: User,
                val zipcode: Int
            ) {

                fun toUserAddress(): UserAddress {
                    return UserAddress(
                        id = id,
                        name = name,
                        country = country,
                        city = city,
                        zipCode = zipcode.toString(),
                        phoneNumber = phoneNumber,
                        addressLine1 = street,
                        addressLine2 = street2
                    )
                }

                data class User(
                    val __v: Int,
                    val _id: String,
                    val active: Boolean,
                    val cart: Cart,
                    val email: String,
                    val id: String,
                    val name: String,
                    val phoneNumber: String,
                    val role: String
                ) {
                    data class Cart(
                        val items: List<Any>
                    )
                }
            }
        }

        fun toSingleOrder(): SingleOrder {
            return SingleOrder(
                id = id,
                orderStatus = orderStatus,
                products = products.map { it.toCartProduct() },
                orderDate = createdAt,
                address = user.location.first().toUserAddress(),
                resitID = uId
            )
        }
    }
}