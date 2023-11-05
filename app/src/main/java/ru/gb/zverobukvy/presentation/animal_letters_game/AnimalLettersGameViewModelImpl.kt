package ru.gb.zverobukvy.presentation.animal_letters_game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.data.resources_provider.ResourcesProvider
import ru.gb.zverobukvy.data.resources_provider.StringEnum
import ru.gb.zverobukvy.data.stopwatch.GameStopwatch
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.use_case.AnimalLettersGameInteractor
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameState.ChangingState
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameState.EntireState
import ru.gb.zverobukvy.utility.ui.SingleEventLiveData
import java.util.LinkedList
import javax.inject.Inject


class AnimalLettersGameViewModelImpl @Inject constructor(
    private val animalLettersGameInteractor: AnimalLettersGameInteractor,
    private val gameStopwatch: GameStopwatch,
    private val provider: ResourcesProvider,
) : AnimalLettersGameViewModel, ViewModel() {

    private var isClickNextWalkingPlayer: Boolean = false

    /** Флаг для события нажатия на карточку с буквой :
     * - true - Произошло нажатие на букву/Обрабатывается событие нажатия (новые нажатия не обрабатываются)
     * - false - Новые нажатия снова обрабатываются
     */
    private var isCardClick: Boolean = false

    /** Флаг для события отгаданного слова :
     * - true - Слово отгадано, ожидается ответ игрока
     */
    private var isGuessedWord: Boolean = false

    /** Флаг для события перехода хода :
     * - true - Ожидается смена игрока
     */
    private var isWaitingNextPlayer: Boolean = false

    /** Хранит идентификатор последней нажатой карточки
     */
    private var mLastClickCardPosition: Int = INIT_CARD_CLICK_POSITION

    /** Последне пришедшее из интерактора состояние
     */
    private var mGameState: GameState? = null

    /** Последне загруженое во View [EntireState], исключая [EntireState.IsEndGameState]
     */
    private var mViewState: EntireState? = null

    /** LiveData для отправки состояний экрана целиком [EntireState]
     */
    private val entireLiveData = MutableLiveData<EntireState>()

    /** LiveData для отправки состояний частичных изменений экрана [ChangingState]
     */
    private val changingLiveData = SingleEventLiveData<ChangingState>()

    /** Инициализация viewModel :
     */
    init {
        viewModelScope.launch {
            animalLettersGameInteractor.startGame()
            animalLettersGameInteractor.subscribeToGameState().collect(::collectGameState)
        }
    }

    /** Метод собирает изменения из Interactor и отправляет их во View
     *
     * @param newState Обновленное состояние игры из Interactor
     */
    private suspend fun collectGameState(newState: GameState?) {
        mGameState.also { oldState ->

            val viewState = convert(oldState, newState)

            viewState.forEachIndexed { index, state ->
                updateViewModels(state)
                calculateDelayBetweenStates(index, viewState)
            }
        }

        updateMGameState(newState)
    }

    /** Метод расчитывает задержку между отправками состояний во View
     *
     * @param viewState Список отправляемых состояний
     * @param index Индекс элемента первого в очереди на отправку
     */
    private suspend fun calculateDelayBetweenStates(
        index: Int,
        viewState: List<AnimalLettersGameState>,
    ) {
        if (index == viewState.lastIndex) {
            return
        }
        if (viewState[index] is ChangingState.NextGuessWord) {
            return
        }
        delay(STATE_DELAY)
    }

    /** Метод обновляет значение сохраняемого во viewModel GameState - [mGameState]
     *
     * @param newState Новое состояние игры/GameState
     */
    private fun updateMGameState(newState: GameState?) {
        mGameState = newState?.copy(
            gameField = newState.gameField.copy(
                gamingWordCard = newState.gameField.gamingWordCard?.copy(
                    positionsGuessedLetters = ArrayList(newState.gameField.gamingWordCard!!.positionsGuessedLetters)
                )
            )
        )
    }

    /** Метод обновляет значение одной из viewModels в зависимости от типа входящего [AnimalLettersGameState]
     * и сохраняет его во viewModel если тип == [EntireState]
     *
     * @param state Состояние экрана для отправки во View
     */
    private fun updateViewModels(state: AnimalLettersGameState) {
        if (state is EntireState) {
            mViewState = state
            entireLiveData.value = mViewState
        } else {
            changingLiveData.value = state as ChangingState
        }
    }

    /** Метод конвертирует разницу состояний (+ [mLastClickCardPosition])
     * в список из одного или двух [AnimalLettersGameState].
     * * Вторым состоянием может быть только [EntireState.EndGameState]
     */
    private suspend fun convert(
        oldState: GameState?,
        newState: GameState?,
    ): List<AnimalLettersGameState> {

        /** Проверка на Null newState :
         * Срабатывает сразу после подписки на данные в Interactor
         */
        if (newState == null) {
            throw IllegalStateException(ERROR_NULL_ARRIVED_GAME_STATE)
        }

        /** Проверка на Null oldState :
         * Срабатывает при первой отправке notNull данных из Interactor
         */
        if (oldState == null) {
            return listOf(
                EntireState.StartGameState(
                    newState.gameField.lettersField,
                    newState.gameField.gamingWordCard!!,
                    newState.players,
                    newState.walkingPlayer!!,
                    isGuessedWord,
                    isWaitingNextPlayer,
                    provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_DEFAULT_SCREEN_DIMMING)
                )
            ).also {
                if (newState.players.any { it.player is Player.ComputerPlayer }) {
                    viewModelScope.launch {
                        animalLettersGameInteractor.subscribeToComputer()
                            .collect(::onComputerClickLetterCard)
                    }

                }

                initComputerStroke(newState)
            }
        }

        /** Список для хранения нескольких стэйтов
         * Подразумевается возможность совмещения [EntireState.EndGameState] и одного из :
         * [ChangingState.CorrectLetter], [ChangingState.InvalidLetter], [ChangingState.GuessedWord]
         * или [ChangingState.NextGuessWord]
         *
         */
        val stateList = LinkedList<AnimalLettersGameState>()

        /** Ловим событие окончания игры [EntireState.EndGameState]
         */
        if (!newState.isActive) {

            stateList.addFirst(
                EntireState.EndGameState(
                    isNonCardClickStateGame(),
                    newState.players,
                    gameStopwatch.getGameRunningTime()
                )
            )
        }

        // Получаем карточки с загаданными словами из двух состояний
        val newWordCard = newState.gameField.gamingWordCard!!
        val oldWordCard = oldState.gameField.gamingWordCard!!

        /** Проверяем равенство загадываемых слов :
         * Ловим событие [ChangingState.NextGuessWord]
         */
        if (newWordCard.word != oldWordCard.word) {

            // Получаем из нового состояния звгадываемое слово и ходящего игрока
            val nextWord = newState.gameField.gamingWordCard
            val nextPlayer = newState.walkingPlayer

            if (nextPlayer != null) {
                stateList.addFirst(
                    ChangingState.NextPlayer(
                        nextPlayer
                    )
                )
            }
            if (nextWord != null) {
                isGuessedWord = false

                stateList.addFirst(
                    ChangingState.NextGuessWord(
                        newWordCard
                    )
                )
                initComputerStroke(newState)
            } else {
                throw IllegalStateException(ERROR_NEXT_GUESSED_WORD_NOT_FOUND)
            }
        }

        if (isClickNextWalkingPlayer) {
            isClickNextWalkingPlayer = false

            val nextWalkingPlayer = newState.walkingPlayer
            val invalidLetterCard = newState.gameField.lettersField[mLastClickCardPosition]

            changingLiveData.value = ChangingState.CloseInvalidLetter(
                invalidLetterCard
            )

            if (nextWalkingPlayer != null) {
                return listOf(
                    ChangingState.NextPlayer(
                        nextWalkingPlayer
                    )
                ).also {
                    initComputerStroke(newState)
                }
            }
        }

        if (isCardClick) {

            // Получем последнюю нажатую карточку
            val lastClickCard = newState.gameField.lettersField[mLastClickCardPosition]

            // Вычисляем позицию последней подсвеченой буквы в загаданном слове
            val positionGuessedLetters = positionGuessedLetters(
                oldWordCard.positionsGuessedLetters,
                newWordCard.positionsGuessedLetters
            )


            if (lastClickCard.isVisible && positionGuessedLetters != null) {

                if (newWordCard.word.length == newWordCard.positionsGuessedLetters.size) {
                    /** Событие отгаданного слова */
                    isCardClick = false

                    if (newState.isActive) {
                        isGuessedWord = true
                    }
                    val screenDimmingText = textOfGuessedWord(newState)

                    stateList.addFirst(
                        ChangingState.GuessedWord(
                            lastClickCard,
                            positionGuessedLetters,
                            newState.players,
                            newState.isActive,
                            screenDimmingText
                        )
                    )
                } else {

                    /** Событие корректно отгаданной буквы */
                    isCardClick = false
                    initComputerStroke(newState)

                    stateList.addFirst(
                        ChangingState.CorrectLetter(
                            lastClickCard,
                            positionGuessedLetters
                        )
                    )
                }

            } else {
                /** Событие неверно отгаданной буквы */

                isCardClick = false
                isWaitingNextPlayer = true

                val screenDimmingText =
                    textOfInvalidLetter(newState)

                stateList.addFirst(ChangingState.InvalidLetter(lastClickCard, screenDimmingText))
            }

        }

        return stateList
    }

    private fun initComputerStroke(currentGameState: GameState) {

        if (currentGameState.walkingPlayer!!.player is Player.ComputerPlayer)
            viewModelScope.launch {
                isCardClick = true

                delay(COMPUTER_DELAY)
                animalLettersGameInteractor.getSelectedLetterCardByComputer()
            }
    }

    private fun textOfInvalidLetter(state: GameState): String {
        return if (isMultiplayerGame(state)) provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_INVALID_LETTER_MULTIPLAYER) + state.nextWalkingPlayer?.player?.name
        else provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_INVALID_LETTER_SINGLE_PLAYER)
    }

    private fun textOfGuessedWord(state: GameState): String {
        return if (isMultiplayerGame(state)) provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_GUESSED_WORD_MULTIPLAYER) + state.nextWalkingPlayer?.player?.name
        else provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_GUESSED_WORD_SINGLE_PLAYER)
    }

    private fun positionGuessedLetters(oldPositions: List<Int>, newPositions: List<Int>): Int? {
        return newPositions.firstOrNull {
            !oldPositions.contains(it)
        }
    }

    override fun onActiveGame() {
        mGameState?.let { state ->
            val guessesWord = state.gameField.gamingWordCard

            val walkingPlayer =
                if (isClickNextWalkingPlayer || isGuessedWord) state.nextWalkingPlayer
                else state.walkingPlayer

            val screenDimmingText =
                calculateScreenDimmingTextOnActiveGame(state)

            if (guessesWord != null && walkingPlayer != null) {
                entireLiveData.value = EntireState.StartGameState(
                    state.gameField.lettersField,
                    state.gameField.gamingWordCard!!,
                    state.players,
                    state.walkingPlayer!!,
                    isGuessedWord,
                    isWaitingNextPlayer,
                    screenDimmingText
                )
            } else {
                throw IllegalStateException(ERROR_STATE_RESTORE)
            }
        }
    }

    private fun calculateScreenDimmingTextOnActiveGame(state: GameState) =
        if (isWaitingNextPlayer) textOfInvalidLetter(state)
        else if (isGuessedWord) textOfGuessedWord(state)
        else provider.getString(StringEnum.GAME_VIEW_MODEL_TEXT_DEFAULT_SCREEN_DIMMING)

    private fun isMultiplayerGame(state: GameState) = state.players.size > 1

    override fun getEntireGameStateLiveData(): LiveData<EntireState> {
        return entireLiveData
    }

    override fun getChangingGameStateLiveData(): SingleEventLiveData<ChangingState> {
        return changingLiveData
    }

    override fun onClickLetterCard(positionSelectedLetterCard: Int) {
        isClickNextWalkingPlayer = false

        val isComputerPlayer: Boolean = mGameState?.walkingPlayer?.player is Player.ComputerPlayer
        if (!isCardClick)
            if (!isComputerPlayer) {

                isCardClick = true
                mLastClickCardPosition = positionSelectedLetterCard
                animalLettersGameInteractor.selectionLetterCard(positionSelectedLetterCard)
            }

    }

    private fun onComputerClickLetterCard(positionSelectedLetterCard: Int) {
        isClickNextWalkingPlayer = false

        isCardClick = true
        mLastClickCardPosition = positionSelectedLetterCard
        animalLettersGameInteractor.selectionLetterCard(positionSelectedLetterCard)


    }

    override fun onClickNextWalkingPlayer() {
        isWaitingNextPlayer = false
        isClickNextWalkingPlayer = true

        animalLettersGameInteractor.getNextWalkingPlayer()
    }

    override fun onClickNextWord() {
        animalLettersGameInteractor.getNextWordCard()
    }

    override fun onBackPressed() {
        gameStopwatch.stop()

        if (isNonCardClickStateGame()) {
            animalLettersGameInteractor.endGameByUser()
        } else {
            entireLiveData.value = EntireState.IsEndGameState
        }
    }

    private fun isNonCardClickStateGame() = mLastClickCardPosition == INIT_CARD_CLICK_POSITION

    override fun onLoadGame() {
        gameStopwatch.start()
    }

    override fun onEndGameByUser() {
        animalLettersGameInteractor.endGameByUser()
    }

    override fun onResume() {
        gameStopwatch.start()
    }

    override fun onPause() {
        gameStopwatch.stop()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel(COROUTINE_SCOPE_CANCEL)
    }

    companion object {
        const val INIT_CARD_CLICK_POSITION = -1

        const val STATE_DELAY = 2000L
        const val COMPUTER_DELAY = 700L

        const val ERROR_NEXT_GUESSED_WORD_NOT_FOUND = "Следующее загадываемое слово не найдено"
        const val ERROR_NULL_ARRIVED_GAME_STATE = "Обновленное состояние GameState == null"
        const val ERROR_STATE_RESTORE = "Проблема в логике восстановления состояния экрана"

        const val COROUTINE_SCOPE_CANCEL = "in viewModel.onCleared()"
    }
}