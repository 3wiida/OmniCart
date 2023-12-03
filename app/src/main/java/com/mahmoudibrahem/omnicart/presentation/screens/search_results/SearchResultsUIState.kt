package com.mahmoudibrahem.omnicart.presentation.screens.search_results

import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class SearchResultsUIState(
    val resultsList: List<CommonProduct> = mutableListOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = false
)
