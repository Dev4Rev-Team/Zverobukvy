package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex

import ru.gb.zverobukvy.domain.entity.GameField
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputer

class AnimalLettersComputerComplex(
    private val smartLevel: Float,
    private var gameField: GameField,
) : AnimalLettersComputer {


    override fun setCurrentGameField(currentGameField: GameField, selectedPosition: Int) {
        TODO("Not yet implemented")
    }

    override fun getSelectedLetterCard(): Int {
        TODO("Not yet implemented")
    }


}

