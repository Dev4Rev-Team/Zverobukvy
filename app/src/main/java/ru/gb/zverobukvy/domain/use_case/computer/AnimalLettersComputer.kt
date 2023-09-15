package ru.gb.zverobukvy.domain.use_case.computer

import ru.gb.zverobukvy.domain.entity.GameField

interface AnimalLettersComputer {
    /**
     * Метод для установки текущего состояния игрового поля
     */
    fun setCurrentGameField(currentGameField: GameField)

    /**
     *
     */
    fun getSelectedLetterCard(): Int
}