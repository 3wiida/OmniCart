package com.mahmoudibrahem.omnicart.core.util

import android.annotation.SuppressLint
import android.util.Log
import com.mahmoudibrahem.omnicart.core.util.Constants.TAG
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

object DateParser {
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    fun parseDate(dateString: String): String {
        var result = ""
        try {
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            date?.let {
                calendar.time = it
            }
            val year = calendar.get(Calendar.YEAR)
            val monthName = date?.let { SimpleDateFormat("MMMM", Locale.getDefault()).format(it) }
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            monthName?.let { result = "$monthName $day, $year" }
        } catch (e: ParseException) {
            Log.d(TAG, "parseDate: ${e.cause}")
        }
        return result
    }
}