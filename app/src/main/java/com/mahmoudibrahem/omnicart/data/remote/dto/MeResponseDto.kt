package com.mahmoudibrahem.omnicart.data.remote.dto

import com.mahmoudibrahem.omnicart.domain.model.User

data class MeResponseDto(
    val user: User
) {
    data class User(
        val __v: Int,
        val _id: String,
        val active: Boolean,
        val email: String,
        val id: String,
        val name: String,
        val phoneNumber: String,
        val role: String
    )

    fun toUser(): com.mahmoudibrahem.omnicart.domain.model.User {
        return User(
            id = user.id,
            email = user.email,
            name = user.name,
            phoneNumber = user.phoneNumber,
            role = user.role,
            active = user.active
        )
    }
}