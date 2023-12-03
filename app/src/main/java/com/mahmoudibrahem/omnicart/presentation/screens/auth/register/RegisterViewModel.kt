package com.mahmoudibrahem.omnicart.presentation.screens.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.Constants.TAG
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onNameChanged(newValue: String) {
        _uiState.update { it.copy(name = newValue) }
    }

    fun onEmailChanged(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue) }
    }

    fun onRegisterButtonClicked() {
        register()
    }

    private fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            registerUseCase(
                name = uiState.value.name,
                email = uiState.value.email,
                password = uiState.value.password,
                confirmPassword = uiState.value.confirmPassword
            ).onResponse(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isRegisterSuccessful = true) }
                    Log.d(TAG, "register: succees")
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d(TAG, "register: $it")
                },
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                    Log.d(TAG, "register: loading")
                }
            )
        }
    }
}