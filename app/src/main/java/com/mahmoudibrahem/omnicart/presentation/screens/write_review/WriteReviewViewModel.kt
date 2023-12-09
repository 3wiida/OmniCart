package com.mahmoudibrahem.omnicart.presentation.screens.write_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.omnicart.core.util.onResponse
import com.mahmoudibrahem.omnicart.domain.usecase.SendReviewUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WriteReviewViewModel @Inject constructor(
    private val sendReviewUseCase: SendReviewUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WriteReviewUIState())
    val uiState = _uiState.asStateFlow()

    fun onSendClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            sendReviewUseCase(
                productID = "",
                review = uiState.value.review,
                rating = uiState.value.rating
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isButtonLoading = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isButtonLoading = false) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isButtonLoading = false) }
                }
            )
        }
    }

    fun onReviewMsgChanged(newValue: String) {
        _uiState.update { it.copy(review = newValue) }
    }

    fun onRatingChanged(newValue: Float) {
        _uiState.update { it.copy(rating = newValue) }
    }
}