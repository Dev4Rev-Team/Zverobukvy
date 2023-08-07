package ru.gb.zverobukvy.domain.use_case.stopwatch

interface TimeFormatter {
    /** Метод форматирует время в миллисекундах в строку
     */
    fun format(timeInMillisecond: Long): String
}