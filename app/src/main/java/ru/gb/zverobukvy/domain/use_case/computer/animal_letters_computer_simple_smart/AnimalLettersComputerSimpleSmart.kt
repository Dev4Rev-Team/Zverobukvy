package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_simple_smart

import ru.gb.zverobukvy.domain.entity.GameField
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputer
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder.FieldHolderSimple
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder.FieldHolderSimpleImpl
import timber.log.Timber
import kotlin.random.Random

/**
 * @param probabilityIsCorrect
 * [0,1] - вероятность правильного хода
 */

class AnimalLettersComputerSimpleSmart(
    private val probabilityIsCorrect: Float,
    gameField: GameField,
) : AnimalLettersComputer {
    private val fieldHolder: FieldHolderSimple = FieldHolderSimpleImpl()
    private val lettersRemember: MutableList<Int> = mutableListOf()
    private val lettersForGame: MutableSet<Int> = mutableSetOf()

    init {
        fieldHolder.update(gameField, -1)
    }

    override fun setCurrentGameField(
        currentGameField: GameField,
        positionLastSelectionLetterCard: Int,
    ) {
        Timber.d("setCurrentGameField, position $positionLastSelectionLetterCard")
        updateLettersRemember(positionLastSelectionLetterCard)
        fieldHolder.update(currentGameField, positionLastSelectionLetterCard)
    }

    override fun getSelectedLetterCard(): Int {
        Timber.d("getSelectedLetterCard")
        updateLettersForGame()
        return lettersForGame.random()
    }

    private fun updateLettersForGame() {
        lettersForGame.clear()
        if (Random.nextFloat() <= probabilityIsCorrect) {
            lettersForGame.addAll(fieldHolder.getInvisibleCorrectLetters())
        } else {
            lettersForGame.addAll(fieldHolder.getIncorrectLetters())
            if (lettersForGame.size > 1) {
                lettersForGame.remove(fieldHolder.getLastPosition())
            }
            if (lettersForGame.size > lettersRemember.size) {
                lettersForGame.removeAll(lettersRemember.toSet())
            }
        }
    }

    private fun updateLettersRemember(selectedPosition: Int) {
        if (selectedPosition >= 0) {
            lettersRemember.add(selectedPosition)
        }
        if (lettersRemember.size >= MAX_REMEMBER) {
            lettersRemember.removeLast()
        }
    }

    companion object {
        const val MAX_REMEMBER = 3
    }

}