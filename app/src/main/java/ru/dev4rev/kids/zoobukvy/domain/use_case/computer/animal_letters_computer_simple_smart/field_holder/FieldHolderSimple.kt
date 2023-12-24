package ru.dev4rev.kids.zoobukvy.domain.use_case.computer.animal_letters_computer_simple_smart.field_holder

import ru.dev4rev.kids.zoobukvy.domain.entity.game_state.GameField

interface FieldHolderSimple {
    fun update(gameField: GameField, lastPosition: Int, blockNewWord:(()->Unit)? = null)
    fun getGuessedWord():Int
    fun getMoveNumberInWord():Int
    fun getLastPosition(): Int
    fun getIncorrectLetters(): Set<Int>
    fun getInvisibleCorrectLetters(): Set<Int>
}