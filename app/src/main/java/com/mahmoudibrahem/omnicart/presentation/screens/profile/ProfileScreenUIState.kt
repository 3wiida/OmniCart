package com.mahmoudibrahem.omnicart.presentation.screens.profile

import com.mahmoudibrahem.omnicart.domain.model.User

data class ProfileScreenUIState(
    val isLoading: Boolean = true,
    val me: User? = null
)
