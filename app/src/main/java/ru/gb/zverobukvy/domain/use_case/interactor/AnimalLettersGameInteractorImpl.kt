package ru.gb.zverobukvy.domain.use_case.interactor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.domain.entity.card.CardsSet
import ru.gb.zverobukvy.domain.entity.game_state.GameField
import ru.gb.zverobukvy.domain.entity.game_state.GameState
import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.player.PlayerInGame
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.card.WordCard
import ru.gb.zverobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository
import ru.gb.zverobukvy.domain.use_case.deal_cards.DealCards
import ru.gb.zverobukvy.domain.use_case.deal_cards.DealCardsImpl
import ru.gb.zverobukvy.domain.use_case.level_and_rating.LevelAndRatingCalculatorImpl
import ru.gb.zverobukvy.domain.use_case.computer.AnimalLettersComputer
import ru.gb.zverobukvy.domain.use_case.computer.animal_letters_computer_simple_smart.AnimalLettersComputerSimpleSmart
import timber.log.Timber
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Возможные состояния игры GameState:
 * 1. загрузка необходимых для игры данных из репозитория: GameState == null
 * 2. начало игры, или начало раунда игры (новое отгадываемое слово), или в ходе игры, пока не
 * отгадана ни одна из букв в слове: GameState (lettersField - для всех карточек-букв
 * isVisible ==  false, gamingWordCard != null и список отгаданных букв в слове пуст
 * (positionsGuessedLetters.isEmpty == true), players - содержит в себе актуальный счет каждого
 * игрока, walkingPlayer != null, nextWalkingPlayer != null, isActive == true)
 * 3. ход игры, когда отгадана одна или несколько букв в слове, но полностью слово еще не отгадано:
 * GameState (lettersField - для отдельных карточек-букв isVisible == true, gamingWordCard != null и
 * список positionsGuessedLetters содержит позиции отгаданных букв, players - содержит в себе
 * актуальный счет каждого игрока, walkingPlayer != null, nextWalkingPlayer != null, isActive == true)
 * 4. отгадано слово (не последнее): GameState (lettersField - для отдельных карточек-букв
 * isVisible == true, gamingWordCard != null и список positionsGuessedLetters содержит позиции всех
 * букв этого слова, players - содержит в себе актуальный счет каждого игрока, walkingPlayer и
 * nextWalkingPlayer соответствуют предшествующему состоянию, isActive = true)
 * 5. отгадано последнее слово (конец игры):GameState (lettersField - для отдельных карточек-букв
 * isVisible == true, gamingWordCard != null и список positionsGuessedLetters содержит позиции всех
 * букв этого слова, players - содержит в себе актуальный счет каждого игрока, walkingPlayer == null,
 * nextWalkingPlayer == null, isActive = false)
 * 6. конец игры по инициативе пользователя во время загрузки данных из репозитория (состояние 1):
 * GameState == null
 * 7.  конец игры по инициативе пользователя во время хода игры (состояния 2-4): GameState
 * (lettersField - listOf, gamingWordCard == null, players - соответствует предшествующему состоянию,
 * walkingPlayer == null, nextWalkingPlayer == null, isActive = false)
 */
class AnimalLettersGameInteractorImpl @Inject constructor(
    private val animalLettersGameRepository: AnimalLettersGameRepository,
    private val changeRatingRepository: ChangeRatingRepository,
    private val typesCards: List<TypeCards>,
    private var players: List<PlayerInGame>
) : AnimalLettersGameInteractor {
    private lateinit var dealCards: DealCards
    private val checkData = CheckData()
    private val calculator =
        LevelAndRatingCalculatorImpl(extractHumanPlayers(players.map { it.player }), typesCards)
    private val gamingWords: Queue<WordCard> = LinkedList()
    private var positionCurrentLetterCard by Delegates.notNull<Int>()
    private var currentWalkingPlayer: PlayerInGame
    private val gameStateFlow: MutableStateFlow<GameState?> = MutableStateFlow(null)
    private val computerSharedFlow: MutableSharedFlow<Int> by lazy {
        MutableSharedFlow<Int>().apply {
            buffer(Channel.RENDEZVOUS)
        }
    }
    private var computer: AnimalLettersComputer? = null

    /**
     * При инициализации проверяются на корректность список типов карточек и список игроков.
     * Инициализируется currentWalkingPlayer
     */
    init {
        // проверяются на корректность, переданные в конструктор данные
        checkData.apply {
            checkTypesCards(typesCards)
            players = getPlayersForGame(players).also {
                // сохраняем в репозитоий состояние игроков в начале игры
                changeRatingRepository.setPlayersBeforeGame(extractHumanPlayers(it.map { playerInGame -> playerInGame.player }))
            }
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
        // получаем все наборы карточек по выбранным цветам (typesCards)  и создаем dealCards
        dealCards = DealCardsImpl(getSelectedColorsCardsSets(typesCards))
        gamingWords.addAll(getGamingWords()) // формируется очередь карточек-слов
        // начальное состояние игры
        gameStateFlow.value = GameState(
            gameField = GameField(
                getStartedLettersField(), // формируется список карточек-букв
                // в данной ситуации очередь карточек-слов не может быть пустой
                gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
            ),
            players = players,
            walkingPlayer = currentWalkingPlayer,
            nextWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer),
            isActive = true
        )
    }

    override fun selectionLetterCard(positionSelectedLetterCard: Int) {
        Timber.d("selectionLetterCard")
        positionCurrentLetterCard = positionSelectedLetterCard
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
                        selectionWrongLetterCard(this@run, positionSelectedLetterCard)
                }
            }
        }
    }

    override fun getNextWordCard() {
        Timber.d("getNextWordCard")
        gameStateFlow.value?.let { currentGameState ->
            // gameStateFlow обновляет value, т.к. отличается gameField, walkingPlayer и nextWalkingPlayer
            val newWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer)
            gameStateFlow.value = currentGameState.copy(
                gameField = GameField(
                    lettersField = mutableListOf<LetterCard>().apply {
                        addAll(currentGameState.gameField.lettersField)
                        forEach { it.isVisible = false }
                    },
                    // в данной ситуации очередь карточек-слов не может быть пустой
                    gamingWordCard = gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
                ).also { computer?.setCurrentGameField(it, positionCurrentLetterCard) },
                walkingPlayer = newWalkingPlayer.also {
                    currentWalkingPlayer = it
                },
                nextWalkingPlayer = getNextWalkingPlayer(players, newWalkingPlayer)
            )
        }
    }

    override fun getNextWalkingPlayer() {
        Timber.d("getNextWalkingPlayer")
        // gameStateFlow обновляет value, т.к. отличается gameField (letterField), walkingPlayer и
        // nextWalkingPlayer (если более одного игрока)
        gameStateFlow.value?.let { currentGameState ->
            val newWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer)
            gameStateFlow.value = currentGameState.copy(
                gameField = GameField(
                    lettersField = flipLetterCard(
                        currentGameState.gameField.lettersField,
                        positionCurrentLetterCard
                    ),
                    gamingWordCard = currentGameState.gameField.gamingWordCard
                ).also { computer?.setCurrentGameField(it, positionCurrentLetterCard) },
                walkingPlayer = newWalkingPlayer.also {
                    currentWalkingPlayer = it
                },
                nextWalkingPlayer = getNextWalkingPlayer(players, newWalkingPlayer)
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
                gameField = GameField(
                    listOf(),
                    null
                ),
                players = players,
                walkingPlayer = null,
                nextWalkingPlayer = null,
                isActive = false
            )
    }

    override suspend fun getSelectedLetterCardByComputer() {
        Timber.d("getSelectedLetterCardByComputer")
        computer?.let {
            computerSharedFlow.emit(it.getSelectedLetterCard())
        }
    }

    override fun subscribeToComputer(): SharedFlow<Int> {
        Timber.d("subscribeToComputer")
        // value в gameStateFlow не может быть null, т.к. этот метод вызывается после
        // получения во viewModel стартового состояния игры
        gameStateFlow.value?.let { gameState ->
            computer = AnimalLettersComputerSimpleSmart.newInstance(
                players.map { it.player },
                gameState.gameField
            )
        }
        return computerSharedFlow
    }

    /**
     * Метод получает из репозитория списки наборов карточек по выбранным цветам typesCards
     * @param typesCards выбранные для игры цвета
     * @return список (от одного до четырех элементов), включающий в себя все наборы по выбранным цветам
     */
    private suspend fun getSelectedColorsCardsSets(typesCards: List<TypeCards>): List<List<CardsSet>> {
        val selectedColorsCardsSets = mutableListOf<List<CardsSet>>()
        typesCards.forEach {
            selectedColorsCardsSets.add(animalLettersGameRepository.getCardsSet(it))
        }
        return selectedColorsCardsSets.toList()
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
        // обновляем уровень текущего игрока
        currentGameState.walkingPlayer?.player?.let {
            if (it is Player.HumanPlayer)
                calculator.updateLettersGuessingLevel(it, true)
        }
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
     * Метод вызывается, если выбрана некорректная карточка-буква, и испускает текущее состоние игры
     * @param currentGameState текущее состояние игры
     * @return текущее состояние игры
     */
    private fun selectionWrongLetterCard(
        currentGameState: GameState,
        positionWrongLetterCard: Int
    ) {
        Timber.d("selectionWrongLetterCard")
        // обновляем уровень текущего игрока
        currentGameState.walkingPlayer?.player?.let {
            if (it is Player.HumanPlayer)
                calculator.updateLettersGuessingLevel(it, false)
        }
        // gameStateFlow обновляет value, т.к. отличается gameField (lettersField)
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = flipLetterCard(
                    currentGameState.gameField.lettersField,
                    positionWrongLetterCard
                ),
                gamingWordCard = currentGameState.gameField.gamingWordCard
            )
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
        // Обновляем рейтинг игрока, отгадавшего букву
        currentGameState.walkingPlayer?.player?.let {
            currentGameState.gameField.gamingWordCard?.let { wordCard ->
                if (it is Player.HumanPlayer)
                    calculator.updateRating(it, wordCard)
            }
        }
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
                lettersField = flipLetterCard(
                    currentGameState.gameField.lettersField,
                    positionCorrectLetterCard
                ),
                gamingWordCard = changeGamingWordCardAfterCorrectLetterCard(
                    currentGameState.gameField.gamingWordCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            ).also { computer?.setCurrentGameField(it, positionCurrentLetterCard) }
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
        // в конце игры сохраняем в БД актуальный рейтинг игроков
        CoroutineScope(Dispatchers.IO).launch {
            calculator.getUpdatedPlayers().forEach {
                animalLettersGameRepository.updatePlayer(it)
            }
        }
        // сохраняем в репозиторий состояние игроков после завершения игры
        changeRatingRepository.setPlayersAfterGame(extractHumanPlayers(players.map { it.player }))
        // gameStateFlow обновляет value, т.к. отличается gameField, players, walkingPlayer,
        //nextWalkingPlayer и isActive
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = flipLetterCard(
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
            nextWalkingPlayer = null,
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
        // gameStateFlow обновляет value, т.к. отличается gameField, player
        gameStateFlow.value = currentGameState.copy(
            gameField = GameField(
                lettersField = flipLetterCard(
                    currentGameState.gameField.lettersField,
                    positionCorrectLetterCard
                ),
                gamingWordCard = changeGamingWordCardAfterCorrectLetterCard(
                    currentGameState.gameField.gamingWordCard,
                    positionCorrectLetterCardInGamingWordCard
                )
            ),
            players = changePlayersAfterGuessedGamingWordCard(currentGameState.players)
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
     * Метод создает список карточек-букв для формирования gameState после переворота карточки-буквы:
     * изменяется isVisible для соответствующей буквы
     * @param currentLetterField текущий список карточек-букв
     * @param positionLetterCard позиция переворачиваемой карточки-буквы
     * @return измененный список карточек-букв
     */
    private fun flipLetterCard(
        currentLetterField: List<LetterCard>,
        positionLetterCard: Int
    ): List<LetterCard> =
        mutableListOf<LetterCard>().apply {
            addAll(currentLetterField)
            this[positionLetterCard] =
                currentLetterField[positionLetterCard].copy(
                    isVisible = !currentLetterField[positionLetterCard].isVisible
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
     * стартового поля игры, исходя из заданного списка из заданного списка с наборами карточе.
     * Дополнительно выполняется проверка на корректность полученных из репозитория данных и
     * данных, сформированных для стартового поля игры.
     * @return список карточек-букв стартового поля игры
     */
    private suspend fun getStartedLettersField(): MutableList<LetterCard> {
        checkData.apply {
            animalLettersGameRepository.getLetterCards().also {
                checkLetterCardsFromRepository(it)
                return checkLettersField(
                    dealCards.getKitLetterCards(it)
                ).toMutableList()
            }
        }
    }

    /**
     * Метод получает из репозитория список всех карточек-слов и формирует очередь карточек-слов
     * для игры, исходя из заданного списка с наборами карточек.
     * Дополнительно выполняется проверка на корректность полученных из репозитория данных и
     * данных, сформированных для игры.
     * @return очередь карточек-слов для игры
     */
    private suspend fun getGamingWords(): Queue<WordCard> {
        checkData.apply {
            animalLettersGameRepository.getWordCards().also {
                checkWordCardsFromRepository(it)
                return LinkedList(
                    checkGamingWords(
                        dealCards.getKitWordCards(it)
                    )
                )
            }
        }
    }

    /**
     * Метод создает из списка Player список только HumanPlayer
     */
    private fun extractHumanPlayers(players: List<Player>): List<Player.HumanPlayer> =
        mutableListOf<Player.HumanPlayer>().apply {
            players.forEach {
                if (it is Player.HumanPlayer)
                    add(it)
            }
        }
}