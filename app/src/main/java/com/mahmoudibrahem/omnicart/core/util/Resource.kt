package com.mahmoudibrahem.omnicart.core.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data = data)
    class Success<T>(data: T) : Resource<T>(data = data)
    class Failure<T>(data: T? = null, message: String) : Resource<T>(data = data, message = message)
}