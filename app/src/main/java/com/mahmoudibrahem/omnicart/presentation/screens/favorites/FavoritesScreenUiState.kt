package com.mahmoudibrahem.omnicart.presentation.screens.favorites

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mahmoudibrahem.omnicart.domain.model.CommonProduct

data class FavoritesScreenUiState(
    val isLoading: Boolean = true,
    val wishlist: SnapshotStateList<CommonProduct> = mutableStateListOf()
)
