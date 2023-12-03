package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.RegisterResponse

data class RegisterResponseDto(
    val `data`: Data,
    val status: String,
    val token: String
) {
    data class Data(
        val user: User
    ) {
        data class User(
            val __v: Int,
            val _id: String,
            val active: Boolean,
            val cart: Cart,
            val email: String,
            val id: String,
            val name: String,
            val role: String
        ) {
            data class Cart(
                val items: List<Any>
            )
        }
    }

    fun toRegisterResponse(): RegisterResponse {
        return RegisterResponse(token = token)
    }
}