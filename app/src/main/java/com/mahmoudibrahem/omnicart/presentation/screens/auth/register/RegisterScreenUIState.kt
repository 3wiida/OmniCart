package com.mahmoudibrahem.omnicart.presentation.screens.auth.register


data class RegisterScreenUIState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false
)