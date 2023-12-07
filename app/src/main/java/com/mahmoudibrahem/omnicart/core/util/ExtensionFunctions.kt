package com.mahmoudibrahem.omnicart.core.util

fun Number?.ifNull(alt: Number): Number {
    return this ?: alt
}