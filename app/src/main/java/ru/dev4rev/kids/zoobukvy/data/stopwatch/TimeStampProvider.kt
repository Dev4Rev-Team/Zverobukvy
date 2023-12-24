package ru.dev4rev.kids.zoobukvy.data.stopwatch

interface TimeStampProvider {
    /** Метод предоставляет актуальное системное время в миллисекундах
     */
    fun getCurrentTime(): Long
}