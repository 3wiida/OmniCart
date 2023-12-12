package com.mahmoudibrahem.omnicart.presentation.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.ChangeQuantityUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.DeleteFromCartUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.GetCartUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.UpsertInWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val upsertInWishlistUseCase: UpsertInWishlistUseCase,
    private val deleteFromCartUseCase: DeleteFromCartUseCase,
    private val changeQuantityUseCase: ChangeQuantityUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CartScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyCart()
    }

    private fun getMyCart() {
        viewModelScope.launch(Dispatchers.IO) {
            getCartUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                cartItems = response!!.toMutableList()
                            )
                        }
                    },
                    onFailure = {}
                )
        }
    }

    fun upsertInWishlist(productID: String) {
        val newList = uiState.value.cartItems.map {
            it.copy(isFav = if (it.id == productID) !it.isFav else it.isFav)
        }
        _uiState.update { it.copy(cartItems = newList.toMutableList()) }
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

    fun deleteFromCart(productID: String) {
        val newList = uiState.value.cartItems.map { it.copy() }.toMutableList()
        newList.removeIf { item -> item.id == productID }
        _uiState.update { it.copy(cartItems = newList) }
        viewModelScope.launch(
            Dispatchers.IO
        ) {
            deleteFromCartUseCase(productID = productID)
                .onResponse(
                    onLoading = {},
                    onSuccess = {},
                    onFailure = {}
                )
        }
    }

    fun onChangeQuantity(productID: String, isIncrease: Boolean) {
        val item = uiState.value.cartItems.find { it.id == productID }
        item?.let {
            if (it.quantity == 1 && !isIncrease){
                return
            }
        }
        val newList = uiState.value.cartItems.map {
            it.copy(
                quantity = if (it.id == productID) {
                    if (isIncrease) {
                        it.quantity + 1
                    } else {
                        it.quantity - 1
                    }
                } else {
                    it.quantity
                }
            )
        }
        _uiState.update { it.copy(cartItems = newList.toMutableList()) }
        viewModelScope.launch(Dispatchers.IO) {
            changeQuantityUseCase(productID = productID, isIncrease = isIncrease)
                .onResponse(
                    onLoading = {},
                    onSuccess = {},
                    onFailure = {}
                )
        }
    }
}