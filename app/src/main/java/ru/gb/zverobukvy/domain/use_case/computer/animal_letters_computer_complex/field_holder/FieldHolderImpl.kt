package ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_complex.field_holder

import ru.gb.zverobukvy.domain.entity.GameField

class FieldHolderImpl : FieldHolder {
    private var word: String = ""
    private var countLetters: Int = 0
    private val invisibleLetters: MutableSet<Int> = mutableSetOf()
    private val incorrectLetters: MutableSet<Int> = mutableSetOf()
    private val invisibleCorrectLetters: MutableSet<Int> = mutableSetOf()
    private var lastPosition: Int = 0
    override fun update(gameField: GameField, lastPosition: Int) {
        val newWord = gameField.gamingWordCard?.word
            ?: throw IllegalArgumentException("gameField.gamingWordCard == null")
        countLetters = gameField.lettersField.size
        this.lastPosition = lastPosition

        if (newWord != word) {
            word = newWord
            incorrectLetters.clear()
            gameField.lettersField.forEachIndexed { index, letterCard ->
                if (!word.contains(letterCard.letter)) {
                    incorrectLetters.add(index)
                }
            }
        }
        invisibleLetters.clear()
        invisibleCorrectLetters.clear()
        gameField.lettersField.forEachIndexed { index, letterCard ->
            if (!letterCard.isVisible) {
                invisibleLetters.add(index)
                if (word.contains(letterCard.letter)) {
                    invisibleCorrectLetters.add(index)
                }
            }
        }

    }

    override fun getLastPosition(): Int = lastPosition

    override fun getWord(): String = word

    override fun getInvisibleLetters(): Set<Int> = invisibleLetters

    override fun getIncorrectLetters(): Set<Int> = incorrectLetters

    override fun gerInvisibleCorrectLetters(): Set<Int> = invisibleCorrectLetters
}