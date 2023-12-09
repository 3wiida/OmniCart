package com.mahmoudibrahem.omnicart.presentation.screens.all_reviews

import com.mahmoudibrahem.omnicart.domain.model.Review

data class AllReviewsUIState(
    val currentFilter: Int = 0,
    val reviews: List<Review> = emptyList(),
    val totalReviews: Int = 0
)