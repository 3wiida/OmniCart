package com.mahmoudibrahem.omnicart.core.util

import com.mahmoudibrahem.omnicart.domain.model.CartItem

fun Number?.ifNull(alt: Number): Number {
    return this ?: alt
}

fun List<CartItem>.calcItemsPrice(): Float {
    var itemsPrice = 0.0f
    this.forEach {
        itemsPrice += (it.discount ?: it.price).toFloat() * it.quantity
    }
    return itemsPrice
}