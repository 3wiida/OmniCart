package com.mahmoudibrahem.omnicart.presentation.screens.write_review

data class WriteReviewUIState(
    val review: String = "",
    val rating: Float = 0.0f,
    val isButtonLoading: Boolean = false
)