package com.mahmoudibrahem.omnicart.presentation.screens.add_address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.AddAddressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAddressViewModel @Inject constructor(
    private val addAddressUseCase: AddAddressUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddAddressScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onAddressNameChanged(newValue: String) {
        _uiState.update { it.copy(addressName = newValue) }
    }

    fun onCountryChanged(newValue: String) {
        _uiState.update { it.copy(country = newValue) }
    }

    fun onCityChanged(newValue: String) {
        _uiState.update { it.copy(city = newValue) }
    }

    fun onAddressLine1Changed(newValue: String) {
        _uiState.update { it.copy(addressLine1 = newValue) }
    }

    fun onAddressLine2Changed(newValue: String) {
        _uiState.update { it.copy(addressLine2 = newValue) }
    }

    fun onZipCodeChanged(newValue: String) {
        _uiState.update { it.copy(zipCode = newValue) }
    }

    fun onPhoneNumberChanged(newValue: String) {
        _uiState.update { it.copy(phoneNumber = newValue) }
    }

    fun addAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            addAddressUseCase(
                addressName = uiState.value.addressName,
                country = uiState.value.country,
                city = uiState.value.city,
                addressLine1 = uiState.value.addressLine1,
                addressLine2 = uiState.value.addressLine2,
                zipCode = uiState.value.zipCode,
                phoneNumber = uiState.value.phoneNumber
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isAddButtonLoading = true) }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isAddButtonLoading = false,
                            isAdditionSuccess = true
                        )
                    }
                },
                onFailure = {}
            )
        }
    }

}
