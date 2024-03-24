package ru.dev4rev.kids.zoobukvy.data.stopwatch

import kotlin.time.Duration

interface TimeFormatter {
    /** Метод форматирует время в строку
     */
    fun formatToString(duration: Duration): String
}
