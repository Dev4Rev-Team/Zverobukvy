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
    private val typesCards: List<TypeCards>,
    private var players: List<Player>
) : AnimalLettersInteractor {
    private val checkData = CheckData()
    private var gameState: GameState? = null
    private val lettersField: MutableList<LetterCard> = mutableListOf()
    private val gamingWords: Queue<WordCard> = LinkedList()
    private val gameStateFlow: MutableStateFlow<GameState?> = MutableStateFlow(gameState)

    /**
     * При инициализации проверяется на корректность список типов карточек и список игроков
     */
    init{
        checkData.apply {
            checkTypesCards(typesCards)
            players = checkPlayers(players)
        }
    }

    override fun subscribeToGameState(): StateFlow<GameState?> =
        gameStateFlow

    override suspend fun startGame() {
        gamingWords.addAll(getGamingWords(typesCards))
        lettersField.addAll(getStartedLettersField(typesCards))
        gameStateFlow.value = GameState(
            GameField(
                lettersField,
                gamingWords.remove()
            ),
            players,
            players.first(),
            true
        ).also {
            gameState = it
        }
    }

    override fun selectionLetterCard(positionSelectedLetterCard: Int) {
        TODO("Not yet implemented")
    }

    override fun endGameByUser() {
        TODO("Not yet implemented")
    }

    /**
     * Метод получает из репозитория список всех карточек-букв и формирует список карточек-букв
     * стартового поля игры, исходя из заданного списка типов карточек (цвета карточек).
     * Дополнительно выполняется проверка на корректность полученных из репозитория данных и
     * данных, сформированных для стартового поля игры.
     * @param typesCards список типов карточек для игры (цвета карточек)
     * @return список карточек-букв стартового поля игры
     */
    private suspend fun getStartedLettersField(typesCards: List<TypeCards>): MutableList<LetterCard> {
        checkData.apply {
            animalLettersCardsRepository.getLetterCards().also {
                checkLetterCardsFromRepository(it)
                return checkLettersField(DealCards.getKitCards(it, typesCards)).toMutableList()
            }
        }
    }

    /**
     * Метод получает из репозитория список всех карточек-слов и формирует очередь карточек-слов
     * для игры, исходя из заданного списка типов карточек (цвета карточек).
     * Дополнительно выполняется проверка на корректность полученных из репозитория данных и
     * данных, сформированных для игры.
     * @param typesCards список типов карточек для игры (цвета карточек)
     * @return очередь карточек-слов для игры
     */
    private suspend fun getGamingWords(typesCards: List<TypeCards>): Queue<WordCard> {
        checkData.apply {
            animalLettersCardsRepository.getWordCards().also {
                checkWordCardsFromRepository(it)
                return LinkedList(checkGamingWords(DealCards.getKitCards(it, typesCards)))
            }
        }
    }
}