package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard

class CheckData {
    /**
     * Метод проверяет полный набор карточек-букв по следующим критериям:
     * - количество карточек-букв должно равняться количестсву букв в алфавите,
     * - каждая карточка-буква должна быть уникальной.
     * @param letterCards полный список карточек-букв
     * @exception IllegalArgumentException
     */
    fun checkLetterCardsFromRepository(letterCards: List<LetterCard>) {
        with(letterCards) {
            if (size != QUANTITY_LETTER_CARDS_FROM_REPOSITORY || !checkUniquenessLettersCards(this))
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_CORRECT)
        }
    }

    /**
     * Метод проверяет, содержит ли список карточек-букв только уникальные буквы без учета регистра
     * @param letterCards список карточек-букв
     * @return true - список карточек-букв уникальный, false - не уникальный
     */
    private fun checkUniquenessLettersCards(letterCards: List<LetterCard>): Boolean {
        val uniquenessLetterCards = letterCards.onEach {
            it.letter.lowercaseChar()
        }.apply {
            distinctBy {
                it.letter
            }
        }
        return letterCards.size == uniquenessLetterCards.size
    }

    /**
     * Метод проверяет полный набор карточек-слов по следующим критериям:
     * - количество карточек-слов должно быть не менее минимального, предусмотренного настольной игрой,
     * - каждая карточка-слово должна быть уникальной,
     * - буквы в каждом слове должны быть уникальными.
     * @param wordCards полный список карточек-слов
     * @exception IllegalArgumentException
     */
    fun checkWordCardsFromRepository(wordCards: List<WordCard>) {
        with(wordCards) {
            if (size < MINIMUM_QUANTITY_WORD_CARDS_FROM_REPOSITORY || !checkUniquenessWordCards(this))
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_CORRECT)
            forEach {
                checkUniquenessLettersInWordCard(it)
            }
        }
    }

    /**
     * Метод проверяет, содержит ли список карточек-слов только уникальные слова без учета регистра
     * @param wordCards список карточек-слов
     * @return true - список карточек-слов уникальный, false - не уникальный
     */
    private fun checkUniquenessWordCards(wordCards: List<WordCard>): Boolean {
        val uniquenessWordCards = wordCards.onEach {
            it.word.lowercase()
        }.apply {
            distinctBy {
                it.word
            }
        }
        return wordCards.size == uniquenessWordCards.size
    }

    /**
     * Метод проверяет,что в слове только уникальные буквы
     * @param wordCard карточка-слово
     * @exception IllegalArgumentException
     */
    private fun checkUniquenessLettersInWordCard(wordCard: WordCard) {
        wordCard.word.lowercase().let {
            if (it.length != it.toSet().size)
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_CORRECT)
        }
    }

    /**
     * Метод проверяет, что список типов карточек (цвета игры) не пустой.
     * @param typesCards список типов карточек (цвета игры)
     * @exception IllegalArgumentException
     */
    fun checkTypesCards(typesCards: List<TypeCards>) {
        if (typesCards.isEmpty())
            throw IllegalArgumentException(TYPES_CARDS_IS_NOT_CORRECT)
    }

    /**
     * Метод проверяет, что список игроков не пустой и обнуляет счет каждого игрока.
     * @param players список игроков
     * @exception IllegalArgumentException
     * @return список игроков
     */
    fun checkPlayers(players: List<Player>): List<Player> {
        with(players) {
            if (isEmpty())
                throw IllegalArgumentException(PLAYERS_IS_NOT_CORRECT)
            return onEach {
                it.scoreInCurrentGame = START_SCORE
            }
        }
    }

    /**
     * Метод проверяет, что количество карточек-слов для конкретной игры не менее минимального,
     * предусмотренного настольной игрой и для каждой карточки переводит слова в нижний регистр и
     * очищает список позиций угаданных букв в данном слове.
     * @param gamingWords список карточек-слов для конкретной игры
     * @exception IllegalArgumentException
     * @return список карточек-слов для конкретной игры
     */
    fun checkGamingWords(gamingWords: List<WordCard>): List<WordCard> {
        with(gamingWords) {
            if (size < MINIMUM_QUANTITY_WORD_CARDS_FOR_START_GAME)
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_CORRECT)
            return this@with.onEach {
                it.word.lowercase()
                it.positionsGuessedLetters.clear()
            }
        }
    }

    /**
     * Метод проверяет, что количество карточек-букв для конкретной игры не менее минимального,
     * предусмотренного настольной игрой и для каждой карточки переводит букву в нижний регистр и
     * устанавливает isVisible = false.
     * @param lettersField список карточек-букв для конкретной игры (игровое поле)
     * @exception IllegalArgumentException
     * @return список карточек-букв для конкретной игры
     */
    fun checkLettersField(lettersField: List<LetterCard>): List<LetterCard> {
        with(lettersField) {
            if (size < MINIMUM_QUANTITY_LETTER_CARDS_FOR_START_GAME)
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_CORRECT)
            return this@with.onEach {
                it.letter.lowercaseChar()
                it.isVisible = false
            }
        }
    }

    /**
     * Метод проверяет, что позиция выбранной карточки-слова корректная, т.е. имеется на игровом поле.
     * @param lettersField список карточук-букв для конкретной игры (игровое поле)
     * @param positionSelectedLetterCard позиция выбранной карточки-слова
     * @exception IllegalArgumentException
     */
    fun checkPositionSelectedLetterCard(
        lettersField: List<LetterCard>,
        positionSelectedLetterCard: Int
    ) {
        if (positionSelectedLetterCard >= lettersField.size)
            throw IllegalArgumentException(POSITION_SELECTED_LETTER_CARD_IS_NOT_CORRECT)
    }

    companion object {
        private const val TYPES_CARDS_IS_NOT_CORRECT = "Некорректный список типа карточек"
        private const val PLAYERS_IS_NOT_CORRECT = "Некорректный список игроков"
        private const val WORD_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-слов"
        private const val LETTER_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-букв"
        private const val POSITION_SELECTED_LETTER_CARD_IS_NOT_CORRECT =
            "Некорректная позиция выбранной карточки-буквы"
        private const val START_SCORE = 0
        private const val MINIMUM_QUANTITY_LETTER_CARDS_FOR_START_GAME = 9
        private const val QUANTITY_LETTER_CARDS_FROM_REPOSITORY = 33
        private const val MINIMUM_QUANTITY_WORD_CARDS_FOR_START_GAME = 8
        private const val MINIMUM_QUANTITY_WORD_CARDS_FROM_REPOSITORY = 33
    }
}