package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_simple_smart

import ru.gb.zverobukvy.domain.entity.game_state.GameField
import ru.gb.zverobukvy.domain.entity.player.Player
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
        if (openRememberLettersFromLastWord()) return
        if (Random.nextFloat() <= probabilityIsCorrect) {
            openCorrectLetters()
        } else {
            openIncorrectLetters()
            if (lettersForGame.size == 0) {
                openCorrectLetters()
            }
        }
    }

    private fun openCorrectLetters() {
        lettersForGame.addAll(fieldHolder.getInvisibleCorrectLetters())
    }

    private fun openIncorrectLetters() {
        lettersForGame.addAll(fieldHolder.getIncorrectLetters())
        lettersForGame.removeAll(lettersRemember.toSet())
        lettersForGame.remove(fieldHolder.getLastPosition())
    }

    private fun openRememberLettersFromLastWord(): Boolean {
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

        const val SMART_COMPUTER = 0.7f
        const val SIZE_ORANGE = 9
        const val SIZE_GREEN = 12
        const val SIZE_BLUE = 15
        const val SIZE_VIOLET = 20
        const val SIZE_MAX = 33

        fun newInstance(gamePlayers: List<Player>, gameField: GameField): AnimalLettersComputer {
            val sizeField = gameField.lettersField.size
            val smartList = mutableListOf<Float>()
            gamePlayers.filter { it !is Player.ComputerPlayer }.forEach {
                val smart = it.rating.violetRating.toFloat()
                smartList.add(smart)
            }
            val probabilityRandom = 1f / sizeField
            return AnimalLettersComputerSimpleSmart(probabilityRandom, gameField)
        }

        fun newInstance(probabilityIsCorrect: Float, gameField: GameField): AnimalLettersComputer {
            return AnimalLettersComputerSimpleSmart(probabilityIsCorrect, gameField)
        }
    }

}