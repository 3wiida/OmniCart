package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.UserAddress

data class UserAddressResponseDto(
    val address: List<Addres>
) {
    data class Addres(
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

        fun toUserAddress(): UserAddress {
            return UserAddress(
                id = _id,
                country = country,
                city = city,
                name = name,
                addressLine2 = street2,
                addressLine1 = street,
                zipCode = zipcode.toString(),
                phoneNumber = phoneNumber
            )
        }
    }
}