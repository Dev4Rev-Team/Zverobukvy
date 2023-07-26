package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.entity.GameField
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard
import java.util.LinkedList
import java.util.Queue

/**
Начальные наброски
 */
class AnimalLettersInteractorImpl(
    private val animalLettersCardsRepository: AnimalLettersCardsRepository,
    override val typesCards: List<TypeCards>,
    override val players: List<Player>
) : AnimalLettersInteractor {
    private var gameState: GameState? = null
    private val lettersField: MutableList<LetterCard> = mutableListOf()
    private val gamingWords: Queue<WordCard> = LinkedList()
    private val gameStateFlow: MutableStateFlow<GameState?> = MutableStateFlow(gameState)

    override fun subscribeToGameState(): StateFlow<GameState?> =
        gameStateFlow

    override suspend fun startGame() {
        gamingWords.addAll(getGamingWords(typesCards))
        lettersField.addAll(getStartedLettersField(typesCards))
        checkDataBeforeStartGame()
        gameState = GameState(
            GameField(
                lettersField,
                gamingWords.remove()
            ),
            players,
            players.first(),
            true
        )
        gameStateFlow.value = gameState
    }

    override fun selectionLetterCard(positionSelectedLetterCard: Int) {
        TODO("Not yet implemented")
    }

    override fun endGameByUser() {
        TODO("Not yet implemented")
    }

    private suspend fun getStartedLettersField(typesCards: List<TypeCards>): MutableList<LetterCard> {
        animalLettersCardsRepository.getLetterCards().also {
            checkLetterCardsFromRepository(it)
            return DealCards.getKitCards(it, typesCards).toMutableList()
        }
    }

    private suspend fun getGamingWords(typesCards: List<TypeCards>): Queue<WordCard> {
        animalLettersCardsRepository.getWordCards().also {
            checkWordCardsFromRepository(it)
            return LinkedList(DealCards.getKitCards(it, typesCards))
        }
    }

    private fun checkLetterCardsFromRepository(lettersCards: List<LetterCard>) {
        with(lettersCards) {
            if (size != ALPHABET_SIZE || size != distinctBy { it.letter }.size)
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_CORRECT)
        }
    }

    private fun checkWordCardsFromRepository(wordCards: List<WordCard>) {
        with(wordCards) {
            if (isEmpty() || size != distinctBy { it.word }.size)
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_CORRECT)
        }
    }

    private fun checkDataBeforeStartGame() {
        if (typesCards.isEmpty())
            throw IllegalArgumentException(TYPES_CARDS_IS_NOT_CORRECT)
        with(players) {
            if (isEmpty())
                throw IllegalArgumentException(PLAYERS_IS_NOT_CORRECT)
            forEach {
                it.scoreInCurrentGame = 0
            }
        }
        with(gamingWords) {
            if (isEmpty())
                throw IllegalArgumentException(WORD_CARDS_IS_NOT_CORRECT)
            forEach {
                it.positionsGuessedLetters.clear()
            }
        }
        with(lettersField) {
            if (isEmpty())
                throw IllegalArgumentException(LETTER_CARDS_IS_NOT_CORRECT)
            forEach {
                it.isVisible = false
            }
        }
    }

    companion object {
        private const val TYPES_CARDS_IS_NOT_CORRECT = "Некорректный список типа карточек"
        private const val PLAYERS_IS_NOT_CORRECT = "Некорректный список игроков"
        private const val WORD_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-слов"
        private const val LETTER_CARDS_IS_NOT_CORRECT = "Некорректный список карточек-букв"
        private const val ALPHABET_SIZE = 33
    }


}