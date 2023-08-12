package ru.gb.zverobukvy.domain.use_case.stopwatch


interface GameStopwatch {

    /** Метод вызывается для начала или продолжения осчета времени игры
     */
    fun start()

    /** Метод вызывается для приотановки осчета времени игры
     */
    fun stop()

    /** Метод предоставляет актуальное время игры
     *
     * @return Время в формате строки
     */
    fun getGameRunningTime(): String
}