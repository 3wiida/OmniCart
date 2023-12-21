package com.mahmoudibrahem.omnicart.presentation.screens.home

import com.mahmoudibrahem.omnicart.domain.model.Category
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class HomeScreenUIState(
    val searchQuery: String = "",
    val categoryList: List<Category> = mutableListOf(),
    val topSalesList: List<CommonProduct> = mutableListOf(),
    val freshSalesList: List<CommonProduct> = mutableListOf(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val searchResultsList: List<String> = mutableListOf(),
    val recommended: List<CommonProduct> = emptyList()
)
