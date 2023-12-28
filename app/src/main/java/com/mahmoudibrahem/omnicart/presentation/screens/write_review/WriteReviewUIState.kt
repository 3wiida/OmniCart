package com.mahmoudibrahem.omnicart.presentation.screens.write_review

data class WriteReviewUIState(
    val review: String = "",
    val rating: String = "",
    val isButtonLoading: Boolean = false,
    val isReviewDone: Boolean = false
)