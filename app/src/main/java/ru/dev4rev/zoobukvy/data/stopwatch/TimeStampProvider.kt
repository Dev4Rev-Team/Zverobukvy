package ru.dev4rev.zoobukvy.data.stopwatch

interface TimeStampProvider {
    /** Метод предоставляет актуальное системное время в миллисекундах
     */
    fun getCurrentTime(): Long
}