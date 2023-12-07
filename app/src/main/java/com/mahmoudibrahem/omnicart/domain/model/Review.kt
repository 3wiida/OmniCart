package com.mahmoudibrahem.omnicart.domain.model

data class Review(
    val username: String,
    val userId: String,
    val rating: Float,
    val review: String,
    val time: String
)