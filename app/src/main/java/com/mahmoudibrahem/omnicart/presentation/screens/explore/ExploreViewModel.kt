package com.mahmoudibrahem.omnicart.presentation.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.model.Category
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
class ExploreViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreScreenUIState())
    val uiState = _uiState.asStateFlow()
    private var searchJob: Job? = null

    fun onSearchQueryChanged(newValue: String) {
        _uiState.update { it.copy(searchQuery = newValue) }
        if (newValue.isEmpty()) {
            searchJob?.cancel()
            _uiState.update { it.copy(searchResultsList = emptyList()) }
        } else {
            searchForProduct()
        }
    }

    private fun searchForProduct() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            searchUseCase(query = uiState.value.searchQuery).onResponse(
                onLoading = {},
                onSuccess = { results ->
                    _uiState.update { it.copy(searchResultsList = results!!.map { product -> product.name }) }
                },
                onFailure = {}
            )
        }
    }

}