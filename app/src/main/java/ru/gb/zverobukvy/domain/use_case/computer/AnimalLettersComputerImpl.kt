package ru.gb.zverobukvy.domain.use_case.computer

import ru.gb.zverobukvy.domain.entity.GameField

class AnimalLettersComputerImpl(
    private val smartLevel: SmartLevel,
    private var gameField: GameField
) : AnimalLettersComputer {
    override fun setCurrentGameField(currentGameField: GameField) {
        TODO("Not yet implemented")
    }

    override fun getSelectedLetterCard(): Int {
        TODO("Not yet implemented")
    }
}