package com.mahmoudibrahem.omnicart.domain.model

data class HomeResponse(
    val categories: List<String>,
    val topSales: List<CommonProduct>,
    val freshSales: List<CommonProduct>
)