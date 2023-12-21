package com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password

data class ForgotPasswordUIState(
    val isSendEmailLoading: Boolean = false,
    val otp: String? = null,
    val isResetPasswordSucceeded: Boolean = false,
    val isResetPasswordLoading: Boolean = false
)