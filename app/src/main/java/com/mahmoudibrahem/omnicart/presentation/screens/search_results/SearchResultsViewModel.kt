package com.mahmoudibrahem.omnicart.presentation.screens.search_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct
import com.mahmoudibrahem.omnicart.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchResultsUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun onSearchQueryChanged(newValue: String) {
        _uiState.update { it.copy(searchQuery = newValue) }
        if (newValue.isEmpty()) {
            searchJob?.cancel()
            _uiState.update { it.copy(resultsList = emptyList()) }
        } else {
            searchForProduct()
        }
    }

    fun sortResults(sortOption: SortOption) {
        val oldList = uiState.value.resultsList.map { it.copy() }
        val newList: List<CommonProduct> = when (sortOption) {
            SortOption.AlphaAsc -> oldList.sortedBy { it.name }
            SortOption.AlphaDes -> oldList.sortedBy { it.name }.reversed()
            SortOption.PriceAcs -> oldList.sortedBy { it.price }
            SortOption.PriceDes -> oldList.sortedBy { it.price }.reversed()
            SortOption.RatingAcs -> oldList.sortedBy { it.rating }
            SortOption.RatingDes -> oldList.sortedBy { it.rating }.reversed()
        }
        _uiState.update { it.copy(resultsList = newList) }
    }

    private fun searchForProduct() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            searchUseCase(query = uiState.value.searchQuery).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onSuccess = { results ->
                    _uiState.update { it.copy(resultsList = results!!, isLoading = false) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun setInitialStartingQuery(value: String) {
        onSearchQueryChanged(newValue = value)
    }
}