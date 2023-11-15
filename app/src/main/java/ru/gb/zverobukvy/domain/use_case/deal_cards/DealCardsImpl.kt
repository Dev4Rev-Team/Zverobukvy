package ru.gb.zverobukvy.domain.use_case.deal_cards

import ru.gb.zverobukvy.configuration.Conf.Companion.MAX_NUMBER_OF_WORD
import ru.gb.zverobukvy.domain.entity.card.CardsSet
import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.card.WordCard

class DealCardsImpl(
    private val selectedColorsCardsSets: List<List<CardsSet>>
): DealCards {

    private val cardsSetsForGame: List<CardsSet> = extractCardsSetsForGame()

    /**
    Метод формирует набор карточек-букв (без повторений) для игры на основе полного набора карточек-букв и наборов карточек
    выбранных для игры.
     * @return список выбранных карточек для игры
     */
    override fun getKitLetterCards(allLetterCards: List<LetterCard>): List<LetterCard> {
        val letterCards = mutableSetOf<LetterCard>()
        cardsSetsForGame.forEach {cardsSet ->
           cardsSet.letters.forEach {letter ->
               letterCards.add(allLetterCards.find { letterCard ->
                   letterCard.letter == letter
               } ?: throw IllegalStateException("$LETTER_CARD_IS_NOT_FIND $letter")
               )
           }
        }
        return letterCards.shuffled()
    }

    /**
    Метод формирует набор карточек-слов (без повторений) для игры на основе полного набора карточек-слов и наборов карточек
    выбранных для игры.
     * @return список выбранных карточек для игры
     */
    override fun getKitWordCards(allWordCards: List<WordCard>): List<WordCard> {
        val wordCards = mutableSetOf<WordCard>()
        cardsSetsForGame.forEach {cardsSet ->
            cardsSet.words.forEach { word ->
                wordCards.find { it.word == word }?.typesCards?.add(cardsSet.typeCards)
                    ?: wordCards.add(extractWordCard(allWordCards, word).apply {
                        typesCards.add(
                            cardsSet.typeCards
                        )
                    })
            }
        }
        return wordCards.shuffled().take(MAX_NUMBER_OF_WORD)
    }

    private fun extractWordCard(
        allWordCards: List<WordCard>,
        word: String,
    ): WordCard =
        allWordCards.find {
            it.word == word
        } ?: throw IllegalStateException("$WORD_CARD_IS_NOT_FIND $word")

    private fun extractCardsSetsForGame(): List<CardsSet>{
        val cardsSetsForGame = mutableListOf<CardsSet>()
        selectedColorsCardsSets.forEach {
            cardsSetsForGame.add(it.shuffled().first())
        }
        return cardsSetsForGame
    }

    companion object {
        private const val LETTER_CARD_IS_NOT_FIND = "Не найдена карточка-буква"
        private const val WORD_CARD_IS_NOT_FIND = "Не найдена карточка-слово"
    }
}