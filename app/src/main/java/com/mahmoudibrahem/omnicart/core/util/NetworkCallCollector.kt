package com.mahmoudibrahem.omnicart.core.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<Resource<T>>.onResponse(
    onSuccess: (T?) -> Unit,
    onFailure: (String?) -> Unit,
    onLoading: () -> Unit
) {
    this.collectLatest { response ->
        when (response) {
            is Resource.Failure -> onFailure(response.message)
            is Resource.Loading -> onLoading()
            is Resource.Success -> onSuccess(response.data)
        }
    }
}