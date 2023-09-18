package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex

import kotlin.math.ceil
import kotlin.math.floor

class AnimalLettersComputerSetting {
    private var posMin: Int = 0
    private var posMax: Int = 0
    private var lvl: Float = 0f
     val slot: Array<Int> = arrayOf(1, 1, 1, 1, 1, 1, 1, 1, 1,1)

    fun setLevel(level: Float) {
        posMin = floor(level).toInt()
        posMax = ceil(level).toInt()
        lvl = level
    }

    fun <T : Number> getValue(arr: Array<T>): Float {
        return (arr[posMax].toFloat() + arr[posMin].toFloat()) / 2
    }

}