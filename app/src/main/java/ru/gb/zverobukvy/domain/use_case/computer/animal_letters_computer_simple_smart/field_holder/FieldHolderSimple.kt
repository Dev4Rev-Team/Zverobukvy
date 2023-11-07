package ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder

import ru.gb.zverobukvy.domain.entity.game_state.GameField

interface FieldHolderSimple {
    fun update(gameField: GameField, lastPosition: Int)
    fun getLastPosition(): Int
    fun getIncorrectLetters(): Set<Int>
    fun getInvisibleCorrectLetters(): Set<Int>
}