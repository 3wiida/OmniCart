package com.mahmoudibrahem.omnicart.presentation.screens.write_review

import android.util.Log
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

    private var productID: String = ""
    private val _uiState = MutableStateFlow(WriteReviewUIState())
    val uiState = _uiState.asStateFlow()

    fun onSendClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            sendReviewUseCase(
                productID = productID,
                review = uiState.value.review,
                rating = uiState.value.rating.toFloat()
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isButtonLoading = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isButtonLoading = false) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isButtonLoading = false, isReviewDone = true) }
                }
            )
        }
    }

    fun setProductID(id: String) {
        productID = id
        Log.d("````TAG````", "setProductID: $productID")
    }

    fun onReviewMsgChanged(newValue: String) {
        _uiState.update { it.copy(review = newValue) }
    }

    fun onRatingChanged(newValue: String) {
        if (newValue.length < 2) {
            if (newValue.isNotEmpty()) {
                if (newValue.toInt() < 6) {
                    _uiState.update { it.copy(rating = newValue) }
                }
            } else {
                _uiState.update { it.copy(rating = newValue) }
            }
        }
    }
}