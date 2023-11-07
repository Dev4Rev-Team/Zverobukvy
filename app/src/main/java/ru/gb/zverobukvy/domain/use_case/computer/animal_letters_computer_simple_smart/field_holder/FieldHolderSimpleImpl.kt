package ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputerSimpleSmart.field_holder

import ru.gb.zverobukvy.domain.entity.game_state.GameField

class FieldHolderSimpleImpl : FieldHolderSimple {
    private var word: String = ""
    private var countLetters: Int = 0
    private val incorrectLetters: MutableSet<Int> = mutableSetOf()
    private val invisibleCorrectLetters: MutableSet<Int> = mutableSetOf()
    private var lastPosition: Int = 0
    private var moveNumberInWord = 0
    private var guessedWord = -1
    override fun update(
        gameField: GameField,
        lastPosition: Int,
        blockNewWord: (() -> Unit)?
    ) {
        val newWord = gameField.gamingWordCard?.word
            ?: throw IllegalArgumentException("gameField.gamingWordCard == null")
        countLetters = gameField.lettersField.size
        this.lastPosition = lastPosition
        moveNumberInWord++
        if (newWord != word) {
            moveNumberInWord = 0
            guessedWord++
            word = newWord
            incorrectLetters.clear()
            gameField.lettersField.forEachIndexed { index, letterCard ->
                if (!word.contains(letterCard.letter)) {
                    incorrectLetters.add(index)
                }
            }
            blockNewWord?.invoke()
        }
        invisibleCorrectLetters.clear()
        gameField.lettersField.forEachIndexed { index, letterCard ->
            if (!letterCard.isVisible && word.contains(letterCard.letter)) {
                invisibleCorrectLetters.add(index)
            }
        }
    }

    override fun getGuessedWord(): Int = guessedWord

    override fun getMoveNumberInWord(): Int = moveNumberInWord

    override fun getLastPosition(): Int = lastPosition

    override fun getIncorrectLetters(): Set<Int> = incorrectLetters

    override fun getInvisibleCorrectLetters(): Set<Int> = invisibleCorrectLetters
}