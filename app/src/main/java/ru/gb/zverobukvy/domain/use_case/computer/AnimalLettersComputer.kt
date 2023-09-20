package ru.gb.zverobukvy.domain.use_case.computer

import ru.gb.zverobukvy.domain.entity.GameField

interface AnimalLettersComputer {
    /**
     * Метод для установки текущего состояния игрового поля
     */
    fun setCurrentGameField(currentGameField: GameField, selectedPosition: Int)

    /**
     * Метод для получения позиции карточки-буквы, которую выбрал для хода компютер
     */
    fun getSelectedLetterCard(): Int
}