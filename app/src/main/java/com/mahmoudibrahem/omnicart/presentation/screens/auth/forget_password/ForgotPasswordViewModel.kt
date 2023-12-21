package com.mahmoudibrahem.omnicart.presentation.screens.auth.forget_password

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.ResetPasswordUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.SendOTPEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val sendOTPEmailUseCase: SendOTPEmailUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun checkOTPMatching(userOTP: String,correctOTP:String): Boolean {
        return userOTP == correctOTP
    }

    fun sendOTPEmail(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendOTPEmailUseCase(email = email)
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isSendEmailLoading = true) }
                    },
                    onSuccess = { otp ->
                        Log.d("```TAG```", "sendOTPEmail: $otp")
                        _uiState.update { it.copy(isSendEmailLoading = false, otp = otp) }
                    },
                    onFailure = {}
                )
        }
    }

    fun resetPassword(
        token:String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase(
                token = token,
                password = password,
                confirmPassword = confirmPassword
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isResetPasswordLoading = true) }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isResetPasswordLoading = false,
                            isResetPasswordSucceeded = true
                        )
                    }
                },
                onFailure = {}
            )
        }
    }
}