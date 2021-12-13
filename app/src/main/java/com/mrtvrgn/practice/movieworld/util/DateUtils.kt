package com.mrtvrgn.practice.movieworld.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

private val defaultDatePattern = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH)
private val mwDatePattern = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)
private val yearPattern = SimpleDateFormat("yyyy", Locale.ENGLISH)

fun String.toYear(): String {
    return try {
        yearPattern.format(defaultDatePattern.parse(this))
    } catch (ex: ParseException) {
        ""
    }
}

fun String.toMonthDayYear(): String {
    return try {
        mwDatePattern.format(defaultDatePattern.parse(this))
    } catch (ex: ParseException) {
        ""
    }
}