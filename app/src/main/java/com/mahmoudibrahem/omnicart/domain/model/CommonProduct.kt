package com.mahmoudibrahem.omnicart.domain.model

data class CommonProduct(
    val id:String,
    val name:String,
    val image:String,
    val disPercentage: Int?,
    val discount: Int?,
    val price: Int
)
