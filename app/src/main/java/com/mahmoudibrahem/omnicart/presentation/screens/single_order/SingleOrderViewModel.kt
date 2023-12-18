package com.mahmoudibrahem.omnicart.presentation.screens.single_order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.GetSingleOrderDetailsUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.UpsertInWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleOrderViewModel @Inject constructor(
    private val getSingleOrderDetailsUseCase: GetSingleOrderDetailsUseCase,
    private val upsertInWishlistUseCase: UpsertInWishlistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SingleOrderScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun getOrder(orderID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSingleOrderDetailsUseCase(orderID = orderID)
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update { it.copy(isLoading = false, order = response) }
                    },
                    onFailure = {}
                )
        }
    }

    fun upsertInWishlist(productID: String) {
        val newList = uiState.value.order?.products?.map {
            it.copy(isFav = if (it.id == productID) !it.isFav else it.isFav)
        }
        _uiState.update { it.copy(order = it.order?.copy(products = newList!!)) }
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            upsertInWishlistUseCase(productID = productID)
                .onResponse(
                    onLoading = {},
                    onFailure = {},
                    onSuccess = {}
                )
        }
    }
}