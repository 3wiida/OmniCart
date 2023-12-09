package com.mahmoudibrahem.omnicart.presentation.screens.all_reviews

import androidx.lifecycle.ViewModel
import com.mahmoudibrahem.omnicart.domain.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AllReviewsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AllReviewsUIState())
    val uiState = _uiState.asStateFlow()

    private var reviewsList: List<Review> = emptyList()

    fun onFilterClicked(numOfStars: Int) {
        when (numOfStars) {
            0 -> _uiState.update {
                it.copy(currentFilter = 0, reviews = reviewsList)
            }

            else -> {
                _uiState.update {
                    it.copy(
                        currentFilter = numOfStars,
                        reviews = reviewsList.filter { review ->
                            review.rating >= numOfStars && review.rating < numOfStars + 1
                        }
                    )
                }
            }
        }
    }

    fun setReviewsList(list: List<Review>) {
        reviewsList = list
        _uiState.update { it.copy(totalReviews = reviewsList.size) }
        onFilterClicked(0)
    }

}