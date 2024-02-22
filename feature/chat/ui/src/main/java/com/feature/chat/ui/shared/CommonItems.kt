package com.feature.chat.ui.shared

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date

interface ContactScreenNavigator{
    fun navigateToChatScreen(id:Int)

}

data class ChatNavArgs(val id: Int)

fun formatDateString(date: Date?): String {
    val inputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val dateString = inputFormatter.format(date ?: LocalDateTime.now())
    val localDateTime =
        LocalDateTime.from(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").parse(dateString))

    val now = LocalDateTime.now()
    val diffDays = ChronoUnit.DAYS.between(localDateTime, now)
    val diffYears = ChronoUnit.YEARS.between(localDateTime, now)

    val outputFormatter: DateTimeFormatter

    val pattern: String = when {
        diffDays == 0L -> "h:mm a" // Today
        diffDays == 1L -> "M/d h:mm a" // Yesterday
        diffYears == 0L -> "M/d h:mm a" // Within the current year
        else -> "M/d/yyyy h:mm a" // Older than a year
    }

    outputFormatter = DateTimeFormatter.ofPattern(pattern)
    return localDateTime.format(outputFormatter)
}