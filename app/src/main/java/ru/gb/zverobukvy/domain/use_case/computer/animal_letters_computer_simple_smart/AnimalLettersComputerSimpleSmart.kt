package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_simple_smart

import ru.gb.zverobukvy.configuration.Conf
import ru.gb.zverobukvy.domain.entity.game_state.GameField
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputer
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder.FieldHolderSimple
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder.FieldHolderSimpleImpl
import timber.log.Timber
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * @param smart
 * [0,infinity] - насколько умней рандома
 */

class AnimalLettersComputerSimpleSmart(
    private val smart: Float,
    gameField: GameField,
) : AnimalLettersComputer {
    private val fieldHolder: FieldHolderSimple = FieldHolderSimpleImpl()
    private val lettersRemember: MutableList<Int> = mutableListOf()
    private val lettersForGame: MutableSet<Int> = mutableSetOf()
    private val sizeTable: Int

    init {
        fieldHolder.update(gameField, NO_SELECT)
        sizeTable = gameField.lettersField.size
        Timber.tag("Computer").d("create  computer smart: $smart ")
    }

    override fun setCurrentGameField(
        currentGameField: GameField,
        positionLastSelectionLetterCard: Int,
    ) {
        updateLettersRemember(positionLastSelectionLetterCard)
        fieldHolder.update(currentGameField, positionLastSelectionLetterCard)
        Timber.tag("Computer").d(
            "setCurrentGameField, position: $positionLastSelectionLetterCard " + "moveNumberInWord: ${fieldHolder.getMoveNumberInWord()}"
        )
    }

    override fun getSelectedLetterCard(): Int {
        Timber.tag("Computer").d(" \n\n getSelectedLetterCard")
        updateLettersForGame()
        return lettersForGame.random()
    }

    private fun updateLettersForGame() {
        val calculationSmartLevel = calculationSmartLevel()
        val calculationProbabilityRandom = calculationProbabilityRandom()
        val probability = calculationProbabilityRandom * calculationSmartLevel * SMART_COMPUTER
        Timber.tag("Computer").d(
            "((ProbabilityRandom:$calculationProbabilityRandom)*(SmartLevel:$calculationSmartLevel)*(SMART_COMPUTER:$SMART_COMPUTER)=probability: $probability"
        )
        lettersForGame.clear()
        if (openRememberLettersFromLastWord()) return
        if (Random.nextFloat() <= probability) {
            openCorrectLetters()
        } else {
            openIncorrectLetters()
            if (lettersForGame.size == 0) {
                openCorrectLetters()
            }
        }
        Timber.tag("Computer").d("\n")
    }

    private fun calculationSmartLevel(): Float {
        val mulMove = min(fieldHolder.getMoveNumberInWord().toFloat() / sizeTable, SMART_MAX_MOVE)
        val mulWord =
            min(fieldHolder.getGuessedWord() * SMART_ADD_FOR_ONE_GUESSED_WORD, SMART_MAX_WORD)
        val smartLevel = 1 + (smart - 1) * mulMove + (smart - 1) * mulWord
        Timber.tag("Computer")
            .d("(smartBase:$smart)  (mulMove:$mulMove)   (mulWord:$mulWord)   ==> smartLevel=$smartLevel")
        return smartLevel
    }

    private fun calculationProbabilityRandom(): Float {
        val correctCard = fieldHolder.getInvisibleCorrectLetters().size
        val invisibleCard = correctCard + fieldHolder.getIncorrectLetters().size -
                    lettersRemember.intersect(fieldHolder.getIncorrectLetters()).size
        return correctCard.toFloat() / invisibleCard
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
        private const val NO_SMART = 1f
        private const val NO_SELECT = -1

        private const val MAX_REMEMBER = Conf.MAX_REMEMBER
        private const val SMART_COMPUTER = Conf.SMART_COMPUTER

        private const val SIZE_ORANGE = Conf.SIZE_ORANGE
        private const val SIZE_GREEN = Conf.SIZE_GREEN
        private const val SIZE_BLUE = Conf.SIZE_BLUE
        private const val SIZE_VIOLET = Conf.SIZE_VIOLET

        private const val SMART_MAX_MOVE = Conf.SMART_MAX_MOVE
        private const val SMART_MAX_WORD = Conf.SMART_MAX_WORD
        private const val SMART_ADD_FOR_ONE_GUESSED_WORD = Conf.SMART_ADD_FOR_ONE_GUESSED_WORD

        private const val AVERAGE_LETTERS_IN_WORD = Conf.AVERAGE_LETTERS_IN_WORD

        fun newInstance(gamePlayers: List<Player>, gameField: GameField): AnimalLettersComputer {
            val sizeField = gameField.lettersField.size
            val smartList = mutableListOf<Float>()
            gamePlayers.filter { it !is Player.ComputerPlayer }.forEach {
                val smart = when {
                    sizeField <= SIZE_ORANGE -> it.lettersGuessingLevel.orangeLevel / (AVERAGE_LETTERS_IN_WORD / SIZE_ORANGE)
                    sizeField <= SIZE_GREEN -> it.lettersGuessingLevel.greenLevel / (AVERAGE_LETTERS_IN_WORD / SIZE_GREEN)
                    sizeField <= SIZE_BLUE -> it.lettersGuessingLevel.blueLevel / (AVERAGE_LETTERS_IN_WORD / SIZE_BLUE)
                    else -> it.lettersGuessingLevel.violetLevel / (AVERAGE_LETTERS_IN_WORD / SIZE_VIOLET)
                }
                smartList.add(smart)
            }
            val smart = max(smartList.average().toFloat(), NO_SMART)
            return AnimalLettersComputerSimpleSmart(smart, gameField)
        }
    }
}
