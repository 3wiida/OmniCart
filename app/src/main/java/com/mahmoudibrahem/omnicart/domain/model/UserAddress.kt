package com.mahmoudibrahem.omnicart.domain.model

data class UserAddress(
    val id: String,
    val name: String,
    val country: String,
    val city: String,
    val zipCode: String,
    val phoneNumber: String,
    val addressLine1: String,
    val addressLine2: String
)