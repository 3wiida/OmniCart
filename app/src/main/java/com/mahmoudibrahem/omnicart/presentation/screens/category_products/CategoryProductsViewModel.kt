package com.mahmoudibrahem.omnicart.presentation.screens.category_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.GetCategoryProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getCategoryProductsUseCase: GetCategoryProductsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryProductsUIState())
    val uiState = _uiState.asStateFlow()

    fun getCategoryProducts(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getCategoryProductsUseCase(categoryName = categoryName)
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update { it.copy(isLoading = false, products = response!!) }
                    },
                    onFailure = {}
                )
        }
    }
}