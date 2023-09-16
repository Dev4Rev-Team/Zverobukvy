package ru.gb.zverobukvy.domain.use_case.computer

import ru.gb.zverobukvy.domain.entity.GameField

/**
 * @param smartLevel
 * [0,1]
 *  0 - тупой
 *  0.5 - случайное открытие
 *  1 - умный
 *  smartLevel = max(1 - percentageOfComputerWins, 0.25)
 */

class AnimalLettersComputerSimple(
    private val smartLevel: Float,
    private var gameField: GameField,
) : AnimalLettersComputer {
    private val lettersInvisible: MutableSet<Int> = mutableSetOf()
    private val lettersRemember: MutableList<Int> = mutableListOf()
    private val lettersForGame: MutableSet<Int> = mutableSetOf()

    override fun setCurrentGameField(currentGameField: GameField, selectedPosition: Int) {
        updateLettersInvisible(currentGameField)
        updateLettersRemember(selectedPosition)
    }

    override fun getSelectedLetterCard(): Int {
        updateLettersForGame()
        return lettersForGame.random()
    }

    private fun updateLettersForGame() {
        lettersForGame.clear()
        lettersForGame.addAll(lettersInvisible)
        lettersForGame.removeAll(lettersRemember.toSet())
    }

    private fun updateLettersRemember(selectedPosition: Int) {
        if (selectedPosition >= 0) {
            lettersRemember.add(selectedPosition)
        }
        if (lettersRemember.size >= MAX_REMEMBER) {
            lettersRemember.removeLast()
        }
    }

    private fun updateLettersInvisible(currentGameField: GameField) {
        lettersInvisible.clear()
        currentGameField.lettersField.forEachIndexed { index, letterCard ->
            if (!letterCard.isVisible) {
                lettersInvisible.add(index)
            }
        }
    }

    companion object {
        const val MAX_REMEMBER = 6
    }

}