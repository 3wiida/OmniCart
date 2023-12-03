package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.LoginResponse

data class LoginResponseDto(
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

    fun toLoginResponse(): LoginResponse {
        return LoginResponse(
            status = status,
            token = token
        )
    }
}