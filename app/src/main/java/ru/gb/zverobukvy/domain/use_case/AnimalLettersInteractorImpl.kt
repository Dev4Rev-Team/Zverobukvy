package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.gb.zverobukvy.domain.entity.GameField
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard
import ru.gb.zverobukvy.domain.repository.AnimalLettersCardsRepository
import timber.log.Timber
import java.util.LinkedList
import java.util.Queue

/**
 * Возможные состояния игры GameState:
 * 1. загрузка необходимых для игры данных из репозитория: GameState == null
 * 2. начало игры, или начало раунда игры (новое отгадываемое слово), или в ходе игры, пока не
 * отгадана ни одна из букв в слове: GameState (lettersField - для всех карточек-букв
 * isVisible ==  false, gamingWordCard != null и список отгаданных букв в слове пуст
 * (positionsGuessedLetters.isEmpty == true), players - содержит в себе актуальный счет каждого
 * игрока, walkingPlayer != null, isActive == true)
 * 3. ход игры, когда отгадана одна или несколько букв в слове, но полностью слово еще не отгадано:
 * GameState (lettersField - для отдельных карточек-букв isVisible == true, gamingWordCard != null и
 * список positionsGuessedLetters содержит позиции отгаданных букв, players - содержит в себе
 * актуальный счет каждого игрока, walkingPlayer != null, isActive == true)
 * 4. отгадано слово (не последнее): GameState (lettersField - для отдельных карточек-букв
 * isVisible == true, gamingWordCard != null и список positionsGuessedLetters содержит позиции всех
 * букв этого слова, players - содержит в себе актуальный счет каждого игрока, walkingPlayer == null,
 * isActive = true)
 * 5. отгадано последнее слово (конец игры):GameState (lettersField - для отдельных карточек-букв
 * isVisible == true, gamingWordCard != null и список positionsGuessedLetters содержит позиции всех
 * букв этого слова, players - содержит в себе актуальный счет каждого игрока, walkingPlayer == null,
 * isActive = false)
 * 6. конец игры по инициативе пользователя во время загрузки данных из репозитория (состояние 1):
 * GameState (lettersField - список карточек-букв пуст, gamingWordCard == null, players - содержит в
 * себе актуальный счет каждого игрока (0), walkingPlayer == null, isActive = false)
 * 7.  конец игры по инициативе пользователя во время хода игры (состояния 2-4): GameState
 * (lettersField - соответствует предшествующему состоянию, gamingWordCard - соответствует
 * предшествующему состоянию, players - соответствует предшествующему состоянию,
 * walkingPlayer - соответствует предшествующему состоянию, isActive = false)
 * !!!Состояние 7 при предшествующем состоянии 4 будет соответствовать состоянию 5. Это необходимо
 * учитывать при формировании viewModel состояния экрана для view!!!
 */
class AnimalLettersInteractorImpl(
    private val animalLettersCardsRepository: AnimalLettersCardsRepository,
    private val typesCards: List<TypeCards>,
    private var players: List<PlayerInGame>
) : AnimalLettersInteractor {
    private val checkData = CheckData()
    private val gamingWords: Queue<WordCard> = LinkedList()
    private var currentWalkingPlayer: PlayerInGame
    private val gameStateFlow: MutableStateFlow<GameState?> = MutableStateFlow(null)

    /**
     * При инициализации проверяются на корректность список типов карточек и список игроков.
     * Инициализируется currentWalkingPlayer
     */
    init {
        // проверяются на корректность, переданные в конструктор данные
        checkData.apply {
            checkTypesCards(typesCards)
            players = checkPlayers(players)
        }
        // определяется игрок, начинающий игру
        currentWalkingPlayer = players.first()
    }

    override fun subscribeToGameState(): StateFlow<GameState?> {
        Timber.d("subscribeToGameState")
        return gameStateFlow.asStateFlow()
    }

    override suspend fun startGame() {
        Timber.d("startGame")
        gamingWords.addAll(getGamingWords(typesCards)) // формируется очередь карточек-слов
        // начальное состояние игры
        gameStateFlow.value = GameState(
            GameField(
                getStartedLettersField(typesCards), // формируется список карточек-букв
                // в данной ситуации очередь карточек-слов не может быть пустой
                gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
            ),
            players,
            currentWalkingPlayer,
            true
        )
    }

    override fun selectionLetterCard(positionSelectedLetterCard: Int) {
        Timber.d("selectionLetterCard")
        gameStateFlow.value?.run {
            // проверяется, что пришла корректная позиция выбранной-карточки
            checkData.checkPositionSelectedLetterCard(
                gameField.lettersField,
                positionSelectedLetterCard
            )
            // в данной ситуации gamingWordCard не может быть null
            gameField.gamingWordCard?.let { gamingWordCard ->
                // запрашивается позиция выбранной буквы в отгадываемом слове
                getPositionSelectedLetterCardInGamingWordCard(
                    gamingWordCard,
                    gameField.lettersField[positionSelectedLetterCard]
                ).also {
                    // выбранная буква содержится в отгадываемом слове
                    if (it >= 0)
                        selectionCorrectLetterCard(
                            this@run,
                            positionSelectedLetterCard,
                            it
                        )
                    // выбранная буква отсутствует в отгадываемом слове
                    else
                        selectionWrongLetterCard(this@run)
                }
            }
        }
    }

    override fun getNextWordCard() {
        Timber.d("getNextWordCard")
        gameStateFlow.value?.let { currentGameState ->
            // gameStateFlow обновляет value, т.к. отличается gameField и walkingPlayer
            gameStateFlow.value = currentGameState.copy(
                gameField = GameField(
                    lettersField = mutableListOf<LetterCard>().apply {
                        addAll(currentGameState.gameField.lettersField)
                        forEach { it.isVisible = false }
                    },
                    // в данной ситуации очередь карточек-слов не может быть пустой
                    gamingWordCard = gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
                ),
                walkingPlayer = getNextWalkingPlayer(
                    currentGameState.players,
                    currentWalkingPlayer
                ).also {
                    currentWalkingPlayer = it
                }
            )
        }
    }

    override fun endGameByUser() {
        Timber.d("endGameByUser")
        // gameStateFlow обновляет value, т.к. отличается isActive
        gameStateFlow.value = gameStateFlow.value?.copy(isActive = false)
                // если gameState == null, значит завершение игры инициировано пользователем, во время
                //загрузки данных из репозитория
            ?: GameState(
                GameField(
                    listOf(),
                    null
                ),
                players,
                null,
                false
            )
    }

    /**
     * Метод вызывается, если выбрана корректная карточка-буква.
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun selectionCorrectLetterCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("selectionCorrectLetterCard")
        // в данной ситуации gamingWordCard не может быть null
        currentGameState.gameField.gamingWordCard?.let {
            // выбранная буква последняя в отгадываемом слове
            if (isLastCorrectLetterCardInGamingWordCard(it))
                selectionLastCorrectLetterCardInGamingWordCard(
                    currentGameState,
                    positionCorrectLetterCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            // выбранная буква не последняя в отгадываемом слове
            else {
                selectionNotLastCorrectLetterCardInGamingWordCard(
                    currentGameState,
                    positionCorrectLetterCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            }
        }
    }

    /**
     * Метод вызывается, если выбрана некорректная карточка-буква, и изменяет текущего игрока
     * @param currentGameState текущее состояние игры
     * @return текущее состояние игры с учетом перехода хода к следующему игроку
     */
    private fun selectionWrongLetterCard(currentGameState: GameState) {
        Timber.d("selectionWrongLetterCard")
        // gameStateFlow обновляет value, т.к. отличается walkingPlayer
        // когда один игрок, педварительно обнуляем walkingPlayer в текущем состоянии gameStateFlow
        if (currentGameState.players.size == 1)
            currentGameState.apply {
                walkingPlayer = null
            }
        gameStateFlow.value = currentGameState.copy(
            walkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer).also {
                currentWalkingPlayer = it
            }
        )
    }

    /**
     * Метод вызывается, если выбранная буква является последней неотгаданной в слове, т.е. слово
     * полностью отгадано.
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun selectionLastCorrectLetterCardInGamingWordCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("selectionLastCorrectLetterCardInGamingWordCard")
        // нет больше карточек-слов для игры
        if (isLastGamingWordCard())
            guessedLastGamingWordCard(
                currentGameState,
                positionCorrectLetterCard,
                positionCorrectLetterCardInGamingWordCard
            )
        // еще есть карточки-слова для игры
        else
            guessedNotLastGamingWordCard(
                currentGameState,
                positionCorrectLetterCard,
                positionCorrectLetterCardInGamingWordCard
            )
    }

    /**
     * Метод вызывается, если выбранная буква является корректной, но не последней неотгаданной в слове,
     * т.е. слово еще полностью не отгадано.
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun selectionNotLastCorrectLetterCardInGamingWordCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("selectionNotLastCorrectLetterCardInGamingWordCard")
        // gameStateFlow обновляет value, т.к. отличается gameField
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = changeLetterFieldAfterCorrectLetterCard(
                    currentGameState.gameField.lettersField,
                    positionCorrectLetterCard
                ),
                gamingWordCard = changeGamingWordCardAfterCorrectLetterCard(
                    currentGameState.gameField.gamingWordCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            )
        )
    }

    /**
     * Метод вызывается, когда отгадано последнее слово и соответственно игра завершается
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun guessedLastGamingWordCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("guessedLastGamingWordCard")
        // gameStateFlow обновляет value, т.к. отличается gameField, players, walkingPlayer и isActive
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = changeLetterFieldAfterCorrectLetterCard(
                    currentGameState.gameField.lettersField,
                    positionCorrectLetterCard
                ),
                gamingWordCard = changeGamingWordCardAfterCorrectLetterCard(
                    currentGameState.gameField.gamingWordCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            ),
            players = changePlayersAfterGuessedGamingWordCard(currentGameState.players),
            walkingPlayer = null,
            isActive = false
        )
    }

    /**
     * Метод вызывается, когда отгадано слово и еще есть слова для продолжения игры
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun guessedNotLastGamingWordCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("guessedNotLastGamingWordCard")
        // gameStateFlow обновляет value, т.к. отличается gameField, player и walkingPlayer
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = changeLetterFieldAfterCorrectLetterCard(
                    currentGameState.gameField.lettersField,
                    positionCorrectLetterCard
                ),
                gamingWordCard = changeGamingWordCardAfterCorrectLetterCard(
                    currentGameState.gameField.gamingWordCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            ),
            players = changePlayersAfterGuessedGamingWordCard(currentGameState.players),
            walkingPlayer = null
        )
    }

    /**
     * Метод создает список игроков для формирования gameState после успешно отгаданного слова:
     * увеличивается счет соответствующего игрока
     * @param currentPlayers текущий список игроков
     * @return измененный список игроков
     */
    private fun changePlayersAfterGuessedGamingWordCard(currentPlayers: List<PlayerInGame>) =
        mutableListOf<PlayerInGame>().apply {
            addAll(currentPlayers)
            this[indexOf(currentWalkingPlayer)].scoreInCurrentGame++
        }

    /**
     * Метод создает отгадываемую карточку-слово для формирования gameState после отгаданной буквы:
     * добавляется позиция отгаданной буквы в список отгаданных буков в слове
     * @param currentGamingWordCard текущая отгадываемая буква-слово
     * @param positionCorrectLetterCardInGamingWordCard позиция отгаданной буквы в слове
     * @return измененная карточка-слово
     */
    private fun changeGamingWordCardAfterCorrectLetterCard(
        currentGamingWordCard: WordCard?,
        positionCorrectLetterCardInGamingWordCard: Int
    ) = currentGamingWordCard?.copy(
        positionsGuessedLetters = mutableListOf<Int>().apply {
            addAll(currentGamingWordCard.positionsGuessedLetters)
            add(positionCorrectLetterCardInGamingWordCard)
        }
    )

    /**
     * Метод создает список карточек-букв для формирования gameState после отгаданной буквы:
     * isVisible = true для соответствующей буквы
     * @param currentLetterField текущий список карточек-букв
     * @param positionCorrectLetterCard позиция отгаданной буквы
     * @return измененный список карточек-букв
     */
    private fun changeLetterFieldAfterCorrectLetterCard(
        currentLetterField: List<LetterCard>,
        positionCorrectLetterCard: Int
    ): List<LetterCard> =
        mutableListOf<LetterCard>().apply {
            addAll(currentLetterField)
            this[positionCorrectLetterCard] =
                currentLetterField[positionCorrectLetterCard].copy(
                    isVisible = true
                )
        }

    /**
     * @param gamingWordCard разгадываемая карточка-слово
     * @param selectedLetterCard выбранная карточка-буква
     * @return позиция выбранной буквы в разгадываемом слове, если буква отсутствует в слове, то -1
     */
    private fun getPositionSelectedLetterCardInGamingWordCard(
        gamingWordCard: WordCard,
        selectedLetterCard: LetterCard
    ): Int = gamingWordCard.word.indexOf(selectedLetterCard.letter, ignoreCase = true)

    /**
     * Метод определяет, что выбранная корректная буква является последней неразгаданной в слове или нет
     * @param gamingWordCard отгадываемая карточка-слово
     * @return true - если в слове осталась не отгадана только одна буква, false - если в слове
     * не отгадано еще несколько букв
     */
    private fun isLastCorrectLetterCardInGamingWordCard(
        gamingWordCard: WordCard
    ) = gamingWordCard.word.length - 1 == gamingWordCard.positionsGuessedLetters.size

    /**
     * @return true - если в очереди карточек-слов больше нет слов, false - еще есть слова для игры
     */
    private fun isLastGamingWordCard() =
        gamingWords.isEmpty()

    /**
     * @param players список игроков
     * @param currentWalkingPlayer текущий игрок
     * @return следующий игрок
     */
    private fun getNextWalkingPlayer(
        players: List<PlayerInGame>,
        currentWalkingPlayer: PlayerInGame
    ): PlayerInGame {
        players.indexOf(currentWalkingPlayer).let {
            if (it < players.size - 1)
                return players[it + 1]
            else
                return players.first()
        }
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