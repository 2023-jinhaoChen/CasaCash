package com.jinhao.casacash

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateParser {
    fun parseDate(dateString: String): Date {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return inputFormat.parse(dateString) ?: Date()
    }
}