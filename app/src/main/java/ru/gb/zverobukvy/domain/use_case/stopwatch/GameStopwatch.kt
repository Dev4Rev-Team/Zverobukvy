package ru.gb.zverobukvy.domain.use_case.stopwatch


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
    fun getGameRunningTime(): String
}