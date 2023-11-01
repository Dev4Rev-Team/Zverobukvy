package ru.gb.zverobukvy.domain.use_case

import ru.gb.zverobukvy.domain.entity.CardsSet
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.WordCard

object DealCards {

    private const val LETTER_CARD_IS_NOT_FIND = "Не найдена карточка-буква"
    private const val WORD_CARD_IS_NOT_FIND = "Не найдена карточка-слово"
    private const val MAX_NUMBER_OF_WORD = 10

    /**
    Метод формирует набор карточек-букв для игры на основе полного набора карточек-букв и наборов карточек
    выбранных для игры цветов.
     * @param allLetterCards список всех карточек-букв
     * @param selectedColorsCardsSets список с набором карточек
     * @return список выбранных карточек для игры
     */
    fun getKitLetterCards(
        allLetterCards: List<LetterCard>,
        selectedColorsCardsSets: List<List<CardsSet>>
    ): List<LetterCard> {
        val letterCards = mutableSetOf<LetterCard>()
        selectedColorsCardsSets.forEach {
            it.shuffled().first().letters.forEach { letter ->
                letterCards.add(allLetterCards.find { letterCard ->
                    letterCard.letter == letter
                } ?: throw IllegalStateException("$LETTER_CARD_IS_NOT_FIND $letter")
                )
            }
        }
        return letterCards.shuffled()
    }

    /**
    Метод формирует набор карточек-слов для игры на основе полного набора карточек-слов и наборов карточек
    выбранных для игры цветов.
     * @param allWordCards список всех карточек-слов
     * @param selectedColorsCardsSets список с набором карточек
     * @return список выбранных карточек для игры
     */
    fun getKitWordCards(
        allWordCards: List<WordCard>,
        selectedColorsCardsSets: List<List<CardsSet>>
    ): List<WordCard> {
        val wordCards = mutableSetOf<WordCard>()
        selectedColorsCardsSets.forEach {
            it.shuffled().first().words.forEach { word ->
                wordCards.add(allWordCards.find { wordCard ->
                    wordCard.word == word
                } ?: throw IllegalStateException("$WORD_CARD_IS_NOT_FIND $word")
                )
            }
        }
        return wordCards.shuffled().take(MAX_NUMBER_OF_WORD)
    }
}