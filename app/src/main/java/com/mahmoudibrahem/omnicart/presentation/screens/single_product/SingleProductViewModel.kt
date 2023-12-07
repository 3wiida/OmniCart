package com.mahmoudibrahem.omnicart.presentation.screens.single_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.AddToCartUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.DeleteFromCartUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.GetSingleProductUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.UpsertInWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SingleProductViewModel @Inject constructor(
    private val getSingleProductUseCase: GetSingleProductUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val upsertInWishlistUseCase: UpsertInWishlistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SingleProductScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onLoveClicked() {
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            upsertInWishlistUseCase(
                productID = uiState.value.productData.productInfo.id
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isUpsertInWishlistLoading = true) }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isInWishlist = !it.isInWishlist,
                            isUpsertInWishlistLoading = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isUpsertInWishlistLoading = false) }
                }
            )
        }
    }

    fun onAddToCartClicked() {
        viewModelScope.launch {
            addToCartUseCase(
                uiState.value.productData.productInfo.id
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isButtonLoading = true) }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isButtonLoading = false,
                            isAddButton = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isButtonLoading = false) }
                }
            )
        }
    }

    fun onDeleteFromCartClicked() {
        viewModelScope.launch {
            deleteFromCartUseCase(
                uiState.value.productData.productInfo.id
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isButtonLoading = true) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isButtonLoading = false, isAddButton = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isButtonLoading = false) }
                }
            )
        }
    }

    fun getProductData(productID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getSingleProductUseCase(
                productID = productID
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onSuccess = { result ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            productData = result!!,
                            isError = false,
                            isInWishlist = result.productInfo.isInFavorites,
                            isAddButton = !result.productInfo.isInCart
                        )
                    }
                },
                onFailure = { msg ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            errorMsg = msg
                        )
                    }
                }
            )
        }
    }
}