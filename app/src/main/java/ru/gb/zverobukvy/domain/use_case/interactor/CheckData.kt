package ru.gb.zverobukvy.domain.use_case.interactor

import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.card.WordCard

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
    private fun checkUniquenessLettersCards(letterCards: List<LetterCard>) =
        letterCards.size == letterCards.distinctBy { it.letter.lowercaseChar() }.size

    /**
     * Метод проверяет полный набор карточек-слов по следующим критериям:
     * - каждая карточка-слово должна быть уникальной,
     * - буквы в каждом слове должны быть уникальными.
     * @param wordCards полный список карточек-слов
     * @exception IllegalArgumentException
     */
    fun checkWordCardsFromRepository(wordCards: List<WordCard>) {
        with(wordCards) {
            if (!checkUniquenessWordCards(this))
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
    private fun checkUniquenessWordCards(wordCards: List<WordCard>) =
        wordCards.size == wordCards.distinctBy { it.word.lowercase() }.size

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

    companion object {
        private const val WORD_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-слов"
        private const val LETTER_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-букв"
        private const val QUANTITY_LETTER_CARDS_FROM_REPOSITORY = 33
    }
}