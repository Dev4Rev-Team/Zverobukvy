package ru.dev4rev.kids.zoobukvy.domain.use_case.computer

import ru.dev4rev.kids.zoobukvy.domain.entity.game_state.GameField

interface AnimalLettersComputer {
    /**
     * Метод для установки текущего состояния игрового поля и передачи последней открытой карточки.
     */
    fun setCurrentGameField(currentGameField: GameField, positionLastSelectionLetterCard: Int)

    /**
     * Метод для получения позиции карточки-буквы, которую выбрал для хода компьютер
     */
    fun getSelectedLetterCard(): Int
}