package com.mahmoudibrahem.omnicart.core.util

import retrofit2.HttpException
import com.mahmoudibrahem.omnicart.domain.model.Error

fun HttpException.parseToErrorModel(): Error {
    return Error("")
}