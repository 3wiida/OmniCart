package com.mahmoudibrahem.omnicart.presentation.screens.auth.login

data class LoginScreenUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val isError: Boolean = false
)
