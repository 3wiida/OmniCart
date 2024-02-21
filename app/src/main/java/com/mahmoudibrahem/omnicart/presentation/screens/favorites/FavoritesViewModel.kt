package com.mahmoudibrahem.omnicart.presentation.screens.favorites

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.GetWishlistUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.UpsertInWishlistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getWishlistUseCase: GetWishlistUseCase,
    private val upsertInWishlistUseCase: UpsertInWishlistUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onLoveClicked(
        productID: String
    ) {
        val itemToRemove = uiState.value.wishlist.find { item -> item.id == productID }
        itemToRemove?.let {
            _uiState.value.wishlist.remove(it)
        }
        viewModelScope.launch(Dispatchers.IO) {
            upsertInWishlistUseCase(productID = productID)
                .onResponse(
                    onLoading = {},
                    onSuccess = {},
                    onFailure = {}
                )
        }
    }

     fun getWishlist() {
        viewModelScope.launch(Dispatchers.IO) {
            getWishlistUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                wishlist = response!!.toMutableStateList()
                            )
                        }
                    },
                    onFailure = {}
                )
        }
    }
}