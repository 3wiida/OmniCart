package com.mahmoudibrahem.omnicart.core.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mahmoudibrahem.omnicart.domain.model.Review

object Converters {
    fun String.fromReviewsJson(): List<Review> {
        return Gson().fromJson(
            this,
            object : TypeToken<ArrayList<Review>>() {}.type
        )
    }

    fun List<Review>.toReviewsJson(): String {
        return Gson().toJson(
            this,
            object : TypeToken<ArrayList<Review>>() {}.type
        )
    }
}