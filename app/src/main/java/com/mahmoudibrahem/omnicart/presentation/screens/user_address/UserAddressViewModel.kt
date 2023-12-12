package com.mahmoudibrahem.omnicart.presentation.screens.user_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.GetUserAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAddressViewModel @Inject constructor(
    private val getUserAddressUseCase: GetUserAddressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(UserAddressScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getUserAddress()
    }

    fun onAddressSelected(addressIndex: Int) {
        _uiState.update { it.copy(selectedAddress = addressIndex) }
    }

    private fun getUserAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            getUserAddressUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update { it.copy(isLoading = false, addressList = response!!) }
                    },
                    onFailure = {}
                )
        }
    }
}