package ru.dev4rev.kids.zoobukvy.data.stopwatch

interface TimeFormatter {
    /** Метод форматирует время в миллисекундах в строку
     */
    fun format(timeInMillisecond: Long): String
}