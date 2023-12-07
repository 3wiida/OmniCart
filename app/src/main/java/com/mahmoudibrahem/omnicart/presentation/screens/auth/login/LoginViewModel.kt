package com.mahmoudibrahem.omnicart.presentation.screens.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.Constants.IS_LOGGED_IN_KEY
import com.mahmoudibrahem.omnicart.core.util.Constants.TAG
import com.mahmoudibrahem.omnicart.core.util.Constants.USER_TOKEN_KEY
import com.mahmoudibrahem.omnicart.core.util.Constants.userToken
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.LoginUseCase
import com.mahmoudibrahem.omnicart.domain.usecase.SaveInDataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveInDataStoreUseCase: SaveInDataStoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
    }

    fun onLoginButtonClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(
                email = uiState.value.email,
                password = uiState.value.password
            ).onResponse(
                onSuccess = { response ->
                    viewModelScope.launch(Dispatchers.IO) {
                        saveInDataStoreUseCase(key = USER_TOKEN_KEY, value = response!!.token)
                        saveInDataStoreUseCase(key = IS_LOGGED_IN_KEY, value = true)
                    }
                    _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
                    userToken = response!!.token
                    Log.d(TAG, "onLoginButtonClicked: $response")
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(isLoading = false) }
                    Log.d(TAG, "onLoginButtonClicked: $msg")
                },
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                    Log.d(TAG, "onLoginButtonClicked: Loading")
                }
            )
        }
    }

}