package ru.gb.zverobukvy.data.check_data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.configuration.Conf
import ru.gb.zverobukvy.domain.entity.card.CardsSet
import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.card.WordCard
import ru.gb.zverobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import timber.log.Timber
import javax.inject.Inject

class CheckDataImpl @Inject constructor(private val repository: AnimalLettersGameRepository) :
    CheckData {
    private val scope = CoroutineScope(Dispatchers.Default)

    override suspend fun checkData() {
        scope.launch {
            checkLetterCardsFromRepository(repository.getLetterCards())
            repository.getWordCards().let {
                checkWordCardsFromRepository(it)
                TypeCards.values().forEach { typeCards ->
                    checkCardsSetFromRepository(repository.getCardsSet(typeCards), it)
                }
            }
            Timber.d(DATA_OK)
        }
    }

    /**
     * Метод проверяет полный набор карточек-букв по следующим критериям:
     * - количество карточек-букв должно равняться количестсву букв в алфавите,
     * - каждая карточка-буква должна быть уникальной.
     * @param letterCards полный список карточек-букв
     * @exception IllegalArgumentException
     */
    private fun checkLetterCardsFromRepository(letterCards: List<LetterCard>) {
        with(letterCards) {
            if (size != QUANTITY_LETTER_CARDS_FROM_REPOSITORY)
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_ENOUGH)
            if (!checkUniquenessLettersCards(this))
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_UNIQUENESS)
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
    private fun checkWordCardsFromRepository(wordCards: List<WordCard>) {
        with(wordCards) {
            if (!checkUniquenessWordCards(this))
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_UNIQUENESS)
            forEach {
                checkUniquenessLettersInWordCard(it)
                checkNumberLettersInWord(it)
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
                throw IllegalArgumentException("$WORD_CARD_IS_NOT_CORRECT ${wordCard.word}")
        }
    }

    /**
     * Метод проверяет количество букв (не больше максимально допустимого)
     * @param wordCard карточка-слово
     * @exception IllegalArgumentException
     */
    private fun checkNumberLettersInWord(wordCard: WordCard) {
        if (wordCard.word.length > Conf.MAX_NUMBER_OF_LETTERS_IN_WORD)
            throw IllegalArgumentException("$WORD_CARD_IS_NOT_CORRECT ${wordCard.word}")
    }

    /**
     * Метод проверяет кард-сеты
     * @param wordCards список карточек-слов
     * @param cardsSets список кард-сетов
     * @exception IllegalArgumentException
     */
    private fun checkCardsSetFromRepository(cardsSets: List<CardsSet>, wordCards: List<WordCard>) {
        cardsSets.forEach {
            checkWordInCardsSet(it, wordCards)
            checkNumberWordsInCardsSet(it)
            checkMatchingLettersToWords(it)
        }
    }

    /**
     * Метод проверяет, что все слова в кард-сете есть в БД
     * @param wordCards список карточек-слов
     * @param cardsSet кард-сет
     * @exception IllegalArgumentException
     */
    private fun checkWordInCardsSet(cardsSet: CardsSet, wordCards: List<WordCard>) {
        cardsSet.words.forEach { word ->
            if (!wordCards.map { it.word }.contains(word))
                throw IllegalArgumentException(
                    WORD_IS_MISSING.format(
                        word,
                        cardsSet.letters.toString()
                    )
                )
        }
    }

    /**
     * Метод проверяет соответствие букв в словах кард-сета с набором букв кард-сета
     * @param cardsSet кард-сет
     * @exception IllegalArgumentException
     */
    private fun checkMatchingLettersToWords(cardsSet: CardsSet) {
        cardsSet.words.forEach { word ->
            word.toCharArray().forEach { char ->
                if (!cardsSet.letters.contains(char))
                    throw IllegalArgumentException(
                        WORD_NOT_MATCH_LETTERS.format(
                            word,
                            cardsSet.letters.toString()
                        )
                    )
            }
        }
    }

    /**
     * Метод проверяет, что кол-во слов в кард-сете не меньше допустимого кол-ва
     * @param cardsSet кард-сет
     * @exception IllegalArgumentException
     */
    private fun checkNumberWordsInCardsSet(cardsSet: CardsSet) {
        if (cardsSet.words.toSet().size < Conf.NUMBER_OF_WORD)
            throw IllegalArgumentException(
                WORDS_IN_CARDS_SET_IS_NOT_ENOUGH.format(
                    cardsSet.letters.toString()
                )
            )
    }

    companion object {
        private const val WORD_CARDS_IS_NOT_UNIQUENESS = "Есть повторяющиеся карточки-слова"
        private const val WORD_CARD_IS_NOT_CORRECT = "Не корректное слово"
        private const val WORD_IS_MISSING = "Отсутствует слово %s из сета %s"
        private const val LETTER_CARDS_IS_NOT_UNIQUENESS = "Есть не уникальные карточки-буквы"
        private const val LETTER_CARDS_IS_NOT_ENOUGH = "Недостаточное количество карточек-букв"
        private const val WORDS_IN_CARDS_SET_IS_NOT_ENOUGH =
            "Недостаточное количество слов в сете %s"
        private const val WORD_NOT_MATCH_LETTERS =
            "Слово %s не соответствует набору букв в сете %s"
        private const val QUANTITY_LETTER_CARDS_FROM_REPOSITORY = 33
        private const val DATA_OK = "Данные по сетам, словам и буквам корректные"
    }
}