package com.mahmoudibrahem.omnicart.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.GetMyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        getMyProfile()
    }

    private fun getMyProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getMyInfoUseCase()
                .onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update { it.copy(isLoading = false, me = response) }
                    },
                    onFailure = {}
                )
        }
    }
}