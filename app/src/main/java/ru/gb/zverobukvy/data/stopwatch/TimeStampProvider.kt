package ru.gb.zverobukvy.data.stopwatch

interface TimeStampProvider {
    /** Метод предоставляет актуальное системное время в миллисекундах
     */
    fun getCurrentTime(): Long
}