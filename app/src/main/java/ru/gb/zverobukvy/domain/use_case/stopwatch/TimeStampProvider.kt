package ru.gb.zverobukvy.domain.use_case.stopwatch

interface TimeStampProvider {
    /** Метод предоставляет актуальное системное время в миллисекундах
     */
    fun getCurrentTime(): Long
}