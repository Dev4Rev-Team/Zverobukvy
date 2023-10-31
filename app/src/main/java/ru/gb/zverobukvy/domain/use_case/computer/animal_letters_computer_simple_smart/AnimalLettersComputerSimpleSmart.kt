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
        fieldHolder.update(gameField, NO_SELECT)
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
        if (isRememberLettersFromLastWord()) return

        if (Random.nextFloat() <= probabilityIsCorrect) {
            lettersForGame.addAll(fieldHolder.getInvisibleCorrectLetters())
        } else {
            lettersForGame.addAll(fieldHolder.getIncorrectLetters())
            lettersForGame.removeAll(lettersRemember.toSet())
            lettersForGame.remove(fieldHolder.getLastPosition())

            if (lettersForGame.size == 0) {
                throw IllegalStateException("Not incorrect letters for game")
            }
        }
    }

    private fun isRememberLettersFromLastWord(): Boolean {
        val intersect = lettersRemember.intersect(fieldHolder.getInvisibleCorrectLetters())
        if (intersect.isNotEmpty()) {
            lettersForGame.addAll(intersect)
            return true
        }
        return false
    }

    private fun updateLettersRemember(selectedPosition: Int) {
        if (selectedPosition >= 0) {
            lettersRemember.add(selectedPosition)
        }
        if (lettersRemember.size > MAX_REMEMBER) {
            lettersRemember.removeFirst()
        }
    }

    companion object {
        const val MAX_REMEMBER = 3
        const val NO_SELECT = -1
    }

}