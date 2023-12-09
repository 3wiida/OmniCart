package com.mahmoudibrahem.omnicart.presentation.screens.explore

import com.mahmoudibrahem.omnicart.domain.model.Category

data class ExploreScreenUIState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val categories: List<Category>? = null,
    val searchResultsList: List<String> = emptyList()
)
