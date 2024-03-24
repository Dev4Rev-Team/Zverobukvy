package ru.dev4rev.kids.zoobukvy.data.stopwatch

import kotlin.time.Duration


interface GameStopwatch {

    /** Метод вызывается для начала или продолжения отсчета времени игры
     */
    fun start()

    /** Метод вызывается для приостановки отсчета времени игры
     */
    fun stop()

    /** Метод предоставляет актуальное время игры
     *
     * @return Время в формате строки
     */
    fun getGameRunningTime(): Duration
}