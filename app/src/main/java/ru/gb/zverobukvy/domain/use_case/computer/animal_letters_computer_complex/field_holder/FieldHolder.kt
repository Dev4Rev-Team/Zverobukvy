package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.field_holder

import ru.gb.zverobukvy.domain.entity.GameField

interface FieldHolder {
    fun update(gameField: GameField, lastPosition: Int)
    fun getLastPosition(): Int
    fun getWord(): String
    fun getInvisibleLetters(): Set<Int>
    fun getIncorrectLetters(): Set<Int>
    fun gerInvisibleCorrectLetters(): Set<Int>
}