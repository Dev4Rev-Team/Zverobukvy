package ru.dev4rev.kids.zoobukvy.domain.use_case.interactor

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
import ru.dev4rev.kids.zoobukvy.domain.entity.card.CardsSet
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard
import ru.dev4rev.kids.zoobukvy.domain.entity.game_state.GameField
import ru.dev4rev.kids.zoobukvy.domain.entity.game_state.GameState
import ru.dev4rev.kids.zoobukvy.domain.entity.game_state.GameStateName
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus
import ru.dev4rev.kids.zoobukvy.domain.repository.ChangeRatingRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game.AnimalLettersGameRepository
import ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters.CalculateColorLetters
import ru.dev4rev.kids.zoobukvy.domain.use_case.calculate_color_letters.CalculateColorLettersImpl
import ru.dev4rev.kids.zoobukvy.domain.use_case.computer.AnimalLettersComputer
import ru.dev4rev.kids.zoobukvy.domain.use_case.computer.animal_letters_computer_simple_smart.AnimalLettersComputerSimpleSmart
import ru.dev4rev.kids.zoobukvy.domain.use_case.deal_cards.DealCards
import ru.dev4rev.kids.zoobukvy.domain.use_case.deal_cards.DealCardsImpl
import ru.dev4rev.kids.zoobukvy.domain.use_case.deal_players.DealPlayersForGame
import ru.dev4rev.kids.zoobukvy.domain.use_case.deal_players.DealPlayersForGameImpl
import ru.dev4rev.kids.zoobukvy.domain.use_case.level_and_rating.LevelAndRatingCalculator
import ru.dev4rev.kids.zoobukvy.domain.use_case.level_and_rating.LevelAndRatingCalculatorImpl
import timber.log.Timber
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import kotlin.properties.Delegates

/**
 * Возможные состояния игры GameState:
 * 1. загрузка необходимых для игры данных из репозитория: GameState == null
 * 2. [GameStateName.START_GAME] начало игры: lettersField - для всех карточек-букв
 * isVisible ==  false и и установлен актуальный цвет, gamingWordCard != null и список отгаданных
 * букв в слове пуст (positionsGuessedLetters.isEmpty == true), players - содержит в себе актуальный
 * счет каждого игрока 0, walkingPlayer != null, nextWalkingPlayer != null, isActive == true
 * 3. [GameStateName.WRONG_LETTER_CARD] выбрана неверная карточка: lettersField - для выбранной
 * карточки-буквы isVisible ==  true, для остальных - из прошлого состояния, gamingWordCard и
 * список отгаданных букв в слове positionsGuessedLetters - из прошлого состояния, players -
 * содержит в себе актуальный счет каждого игрока из прошлого состояния, walkingPlayer и
 * nextWalkingPlayer - из прошлого состояния, isActive == true
 * 4. [GameStateName.NEXT_WALKING_PLAYER_AFTER_WRONG_LETTER_CARD] переход хода к следующему игроку
 * после выбранной неверной карточки: lettersField - для выбранной неверной карточки-буквы
 * isVisible ==  false, для остальных - из прошлого состояния, gamingWordCard и список отгаданных
 * букв в слове positionsGuessedLetters - из прошлого состояния, players - содержит в себе
 * актуальный счет каждого игрока из прошлого состояния, walkingPlayer и nextWalkingPlayer
 * изменяются на следующих по порядку, isActive == true
 * 5.[GameStateName.NOT_LAST_CORRECT_LETTER_CARD] выбрана верная карточка не последняя в слове:
 * lettersField - для выбранной карточки-буквы isVisible ==  true, для остальных - из прошлого
 * состояния, в gamingWordCard изменен список отгаданных букв в слове positionsGuessedLetters,
 * players - содержит в себе актуальный счет каждого игрока из прошлого состояния, walkingPlayer и
 * nextWalkingPlayer - из прошлого состояния, isActive == true
 * 6. [GameStateName.GUESSED_WORD_CARD] выбрана верная карточка последняя в слове: lettersField -
 * для выбранной карточки-буквы isVisible ==  true, для остальных - из прошлого состояния,
 * в gamingWordCard изменен список отгаданных букв в слове positionsGuessedLetters, players -
 * содержит в себе актуальный счет каждого игрока: для ходящего - обвновлен (+1), для остальных - из
 * прошлого состояния, walkingPlayer и nextWalkingPlayer - из прошлого состояния, isActive == true
 * 7. [GameStateName.NEXT_WORD_CARD_AND_NEXT_WALKING_PLAYER] новое слово и переход хода к следующему
 * игроку: lettersField - для всех карточек-букв isVisible ==  false и установлен актуальный цвет,
 * в gamingWordCard - новое слово и список отгаданных букв в слове пуст, players - содержит в себе
 * актуальный счет каждого игрока из прошлого состояния, walkingPlayer и nextWalkingPlayer изменяются
 * на следующих по порядку, isActive == true
 * 8. [GameStateName.END_GAME] больше нет карточек-слов для отгадывания, конец игры:
 * lettersField - из прошлого состояния, gamingWordCard == null, players - содержит в себе
 * актуальный счет каждого игрока из прошлого состояния (но отсортирован по убыванию),
 * walkingPlayer == null и nextWalkingPlayer == null, isActive == false
 * 9. [GameStateName.END_GAME_BY_USER] игра завершена пользователем (не доиграл): все поля из
 * прошлого состояния, кроме isActive == false
 * 10. [GameStateName.UPDATE_LETTER_CARD] обновление цвета карточек из-за смены озвучки букв/звуков:
 * все поля - из прошлого состояния, кроме lettersField - для всех карточек-букв установлен актуальный
 * цвет, изменен voiceActingStatus.
 */

class AnimalLettersGameInteractorImpl @Inject constructor(
    private val animalLettersGameRepository: AnimalLettersGameRepository,
    private val changeRatingRepository: ChangeRatingRepository,
    private val typesCards: List<TypeCards>,
    private var players: List<PlayerInGame>
) : AnimalLettersGameInteractor {
    private lateinit var dealCards: DealCards
    private val dealPlayersForGame: DealPlayersForGame = DealPlayersForGameImpl()
    private var levelAndRatingCalculator: LevelAndRatingCalculator
    private val colorLettersCalculator: CalculateColorLetters = CalculateColorLettersImpl()
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

    init {
        // подготавливаем список игроков для начала игры
        players = dealPlayersForGame.getPlayersForGame(players).also { playersInGame ->
            dealPlayersForGame.extractHumanPlayers(playersInGame.map { it.player }).let {
                levelAndRatingCalculator = LevelAndRatingCalculatorImpl(it, typesCards)
                // сохраняем в репозитоий состояние игроков в начале игры
                changeRatingRepository.setPlayersBeforeGame(it)
            }
        }
        // определяется игрок, начинающий игру
        currentWalkingPlayer = players.first()
    }

    override fun subscribeToGameState(): StateFlow<GameState?> {
        Timber.d("subscribeToGameState")
        return gameStateFlow.asStateFlow()
    }

    override suspend fun startGame(voiceActingStatus: VoiceActingStatus) {
        Timber.d("startGame")
        // получаем все наборы карточек по выбранным цветам (typesCards)  и создаем dealCards
        dealCards = DealCardsImpl(getSelectedColorsCardsSets(typesCards))
        gamingWords.addAll(getGamingWords()) // формируется очередь карточек-слов
        // начальное состояние игры
        gameStateFlow.value = GameState(
            name = GameStateName.START_GAME,
            gameField = GameField(
                getStartedLettersField(), // формируется список карточек-букв
                // в данной ситуации очередь карточек-слов не может быть пустой
                gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
            ).apply {
                // определяем цвет букв и озвучку звуков (твердые/мягкие) и обновляем lettersField
                gamingWordCard?.let {
                    colorLettersCalculator.calculate(it.word, lettersField, voiceActingStatus)
                }
            },
            players = players,
            walkingPlayer = currentWalkingPlayer,
            nextWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer),
            isActive = true,
            voiceActingStatus = voiceActingStatus
        )
    }

    override fun selectionLetterCard(positionSelectedLetterCard: Int) {
        Timber.d("selectionLetterCard")
        positionCurrentLetterCard = positionSelectedLetterCard
        gameStateFlow.value?.run {
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
            if (isHasGamingWordCard())
                endGame(currentGameState)
            else {
                nextWordCard(currentGameState)
            }
        }
    }

    override fun getNextWalkingPlayer() {
        Timber.d("getNextWalkingPlayer")
        // gameStateFlow обновляет value, т.к. отличается gameField (letterField), walkingPlayer и
        // nextWalkingPlayer (если более одного игрока)
        gameStateFlow.value?.let { currentGameState ->
            val newWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer)
            gameStateFlow.value = currentGameState.copy(
                name = GameStateName.NEXT_WALKING_PLAYER_AFTER_WRONG_LETTER_CARD,
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
        gameStateFlow.value = gameStateFlow.value?.copy(
            name = GameStateName.END_GAME_BY_USER,
            isActive = false
        )
                // если gameState == null, значит завершение игры инициировано пользователем, во время
                //загрузки данных из репозитория
            ?: GameState(
                name = GameStateName.END_GAME_BY_USER,
                gameField = GameField(
                    listOf(),
                    null
                ),
                players = players,
                walkingPlayer = null,
                nextWalkingPlayer = null,
                isActive = false,
                voiceActingStatus = VoiceActingStatus.SOUND
            )
        // в конце игры "отматываем" рейтинг игроков к исходному
        changeRatingRepository.getPlayersBeforeGame().forEach { playerBeforeGame ->
            players.find { playerBeforeGame.id == it.player.id }?.player?.apply {
                rating = playerBeforeGame.rating
                lettersGuessingLevel = playerBeforeGame.lettersGuessingLevel
            }
        }
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

    override fun updateVoiceActingStatus(voiceActingStatus: VoiceActingStatus) {
        gameStateFlow.value?.let { currentGameState ->
            // gameStateFlow обновляет value, т.к. отличается voiceActingStatus
            gameStateFlow.value = currentGameState.copy(
                name = GameStateName.UPDATE_LETTER_CARD,
                gameField = GameField(
                    lettersField =
                    currentGameState.gameField.lettersField.also { lettersField ->
                        currentGameState.gameField.gamingWordCard?.word?.let {
                            colorLettersCalculator.calculate(
                                it, lettersField, voiceActingStatus
                            )
                        }
                    },
                    gamingWordCard = currentGameState.gameField.gamingWordCard
                ),
                voiceActingStatus = voiceActingStatus
            )
        }
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
                levelAndRatingCalculator.updateLettersGuessingLevel(it, true)
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
                levelAndRatingCalculator.updateLettersGuessingLevel(it, false)
        }
        // gameStateFlow обновляет value, т.к. отличается gameField (lettersField)
        gameStateFlow.value = currentGameState.copy(
            name = GameStateName.WRONG_LETTER_CARD,
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
                    levelAndRatingCalculator.updateRating(it, wordCard)
            }
        }
        guessedGamingWordCard(
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
            name = GameStateName.NOT_LAST_CORRECT_LETTER_CARD,
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
     * Метод вызывается, когда больше нет карточек-слов для отгадывания
     * @param currentGameState текущее состояние игры
     */
    private fun endGame(
        currentGameState: GameState
    ) {
        Timber.d("endGame")
        // в конце игры сохраняем в БД актуальный рейтинг игроков
        CoroutineScope(Dispatchers.IO).launch {
            levelAndRatingCalculator.getUpdatedPlayers().forEach {
                animalLettersGameRepository.updatePlayer(it)
            }
        }
        // сохраняем в репозиторий состояние игроков после завершения игры
        changeRatingRepository.setPlayersAfterGame(dealPlayersForGame.extractHumanPlayers(players.map { it.player }))
        // gameStateFlow обновляет value, т.к. отличается gameField, walkingPlayer, nextWalkingPlayer
        // и isActive
        gameStateFlow.value = currentGameState.copy(
            name = GameStateName.END_GAME,
            gameField = GameField(
                lettersField = currentGameState.gameField.lettersField,
                gamingWordCard = null
            ),
            players = currentGameState.players.toMutableList().apply {
                // сортировка игроков по кол-ву отгаданных слов (по убыванию)
                sortBy {
                    -it.scoreInCurrentGame
                }
            },
            walkingPlayer = null,
            nextWalkingPlayer = null,
            isActive = false
        )
    }

    /**
     * Метод вызывается, когда запрашивается следующая карточка-слово для отгадывания и она есть
     * @param currentGameState текущее состояние игры
     */
    private fun nextWordCard(currentGameState: GameState) {
        Timber.d("nextWordCard")
        // gameStateFlow обновляет value, т.к. отличается gameField, walkingPlayer и nextWalkingPlayer
        val newWalkingPlayer = getNextWalkingPlayer(players, currentWalkingPlayer)
        gameStateFlow.value = currentGameState.copy(
            name = GameStateName.NEXT_WORD_CARD_AND_NEXT_WALKING_PLAYER,
            gameField = GameField(
                lettersField = mutableListOf<LetterCard>().apply {
                    addAll(currentGameState.gameField.lettersField)
                    forEach { it.isVisible = false }
                },
                // в данной ситуации очередь карточек-слов не может быть пустой
                gamingWordCard = gamingWords.remove() // отгадываемая карточка-слово удаляется из очереди
            ).also {
                computer?.setCurrentGameField(it, positionCurrentLetterCard)
                // определяем цвет букв и озвучку звуков (твердые/мягкие) и обновляем lettersField
                it.gamingWordCard?.let { wordCard ->
                    colorLettersCalculator.calculate(
                        wordCard.word,
                        it.lettersField,
                        currentGameState.voiceActingStatus
                    )
                }
            },
            walkingPlayer = newWalkingPlayer.also {
                currentWalkingPlayer = it
            },
            nextWalkingPlayer = getNextWalkingPlayer(players, newWalkingPlayer)
        )
    }

    /**
     * Метод вызывается, когда отгадано слово
     * @param currentGameState текущее состояние игры
     * @param positionCorrectLetterCard позиция корректной карточки-буквы в списке карточек-букв
     * @param positionCorrectLetterCardInGamingWordCard позиция корректной буквы в отгадываемом слове
     */
    private fun guessedGamingWordCard(
        currentGameState: GameState,
        positionCorrectLetterCard: Int,
        positionCorrectLetterCardInGamingWordCard: Int
    ) {
        Timber.d("guessedGamingWordCard")
        // gameStateFlow обновляет value, т.к. отличается gameField, players
        gameStateFlow.value = currentGameState.copy(
            name = GameStateName.GUESSED_WORD_CARD,
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
    private fun isHasGamingWordCard() =
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
     * стартового поля игры, исходя из заданного списка с наборами карточе.
     * @return список карточек-букв стартового поля игры
     */
    private suspend fun getStartedLettersField(): MutableList<LetterCard> =
        dealCards.getKitLetterCards(animalLettersGameRepository.getLetterCards()).toMutableList()

    /**
     * Метод получает из репозитория список всех карточек-слов и формирует очередь карточек-слов
     * для игры, исходя из заданного списка с наборами карточек.
     * @return очередь карточек-слов для игры
     */
    private suspend fun getGamingWords(): Queue<WordCard> =
        LinkedList(dealCards.getKitWordCards(animalLettersGameRepository.getWordCards()))
}