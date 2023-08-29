package ru.gb.zverobukvy.presentation.game_zverobukvy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameState.ChangingState
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameState.EntireState
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.use_case.AnimalLettersGameInteractor
import ru.gb.zverobukvy.domain.use_case.stopwatch.GameStopwatch
import ru.gb.zverobukvy.utility.ui.SingleEventLiveData
import java.util.LinkedList
import javax.inject.Inject


class AnimalLettersGameViewModelImpl @Inject constructor(
    private val animalLettersInteractor: AnimalLettersInteractor,
    private val gameStopwatch: GameStopwatch,
) :
    AnimalLettersGameViewModel, ViewModel() {

    /** Флаг показа диалогового окна о закрытии игры :
     * - true - показывается диалог об окончании игры
     * - false - диалог скрыт
     */
    private var isEndGameDialog: Boolean = false

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

            //checkingCorrectnessOfConversion(viewState)

            viewState.forEachIndexed { index, state ->
                updateViewModels(state)
                if (index != viewState.lastIndex) delay(STATE_DELAY)
            }
        }

        updateMGameState(newState)
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

    /** Метод проверяет результат конвертации на наличие логических ошибок
     *
     * @param viewState Результат конвертации для проверки
     *
     * @exception IllegalStateException если проверка провалена
     */
    private fun checkingCorrectnessOfConversion(
        viewState: List<AnimalLettersGameState>,
    ) {
        if (viewState.size > 3)
            throw IllegalStateException(ERROR_INDEX_INCORRECT)

        if ((viewState.size > 1) &&
            (viewState[1] !is EntireState.EndGameState || viewState[1] !is ChangingState.NextPlayer)
        )
            throw IllegalStateException(ERROR_INCORRECT_TYPE_OF_APP_STATE)

        if (viewState.size > 2 && viewState[2] !is EntireState.EndGameState
        )
            throw IllegalStateException(ERROR_INCORRECT_TYPE_OF_APP_STATE)
    }

    /** Метод конвертирует разницу состояний (+ [mLastClickCardPosition])
     * в список из одного или двух [AnimalLettersGameState].
     * * Вторым состоянием может быть только [EntireState.EndGameState]
     */
    private fun convert(oldState: GameState?, newState: GameState?): List<AnimalLettersGameState> {

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
                )
            )
        }

        /** Список для хранения нескольких стэйтов
         * Подразумевается возможность совмещения [EntireState.EndGameState] и одного из :
         * [ChangingState.CorrectLetter], [ChangingState.InvalidLetter], [ChangingState.GuessedWord]
         * или [ChangingState.NextGuessWord]
         *
         * TODO : Теоретически возможно совмещение [EntireState.EndGameState] и с двумя первыми проверками
         */
        val stateList = LinkedList<AnimalLettersGameState>()

        /** Ловим событие окончания игры [EntireState.EndGameState]
         */
        if (!newState.isActive) {
            stateList.addFirst(
                EntireState.EndGameState(
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
            } else {
                throw IllegalStateException(ERROR_NEXT_GUESSED_WORD_NOT_FOUND)
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
                    if (newState.isActive) isGuessedWord = true

                    stateList.addFirst(
                        ChangingState.GuessedWord(
                            lastClickCard,
                            positionGuessedLetters,
                            newState.players,
                            newState.isActive
                        )
                    )
                } else {
                    /** Событие корректно отгаданной буквы */
                    isCardClick = false

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

                stateList.addFirst(ChangingState.InvalidLetter(lastClickCard))
            }

        }

        return stateList
    }

    private fun positionGuessedLetters(oldPositions: List<Int>, newPositions: List<Int>): Int? {
        return newPositions.firstOrNull {
            !oldPositions.contains(it)
        }
    }

    override fun onActiveGame() {
        mGameState?.let { state ->
            val guessesWord = state.gameField.gamingWordCard
            val walkingPlayer = state.walkingPlayer

            if (guessesWord != null && walkingPlayer != null) {
                entireLiveData.value = EntireState.StartGameState(
                    state.gameField.lettersField.apply {
                        if (isWaitingNextPlayer) {
                            this[mLastClickCardPosition].isVisible = true
                        }
                    },
                    state.gameField.gamingWordCard!!,
                    state.players,
                    state.walkingPlayer!!,
                    isGuessedWord,
                    isWaitingNextPlayer,
                )
            } else {
                throw IllegalStateException(ERROR_STATE_RESTORE)
            }
        }

        if (isEndGameDialog)
            entireLiveData.value = EntireState.IsEndGameState
    }

    override fun getEntireGameStateLiveData(): LiveData<EntireState> {
        return entireLiveData
    }

    override fun getChangingGameStateLiveData(): SingleEventLiveData<ChangingState> {
        return changingLiveData
    }

    override fun onClickLetterCard(positionSelectedLetterCard: Int) {
        if (!isCardClick) {
            isCardClick = true
            mLastClickCardPosition = positionSelectedLetterCard
            animalLettersGameInteractor.selectionLetterCard(positionSelectedLetterCard)
        }

    }

    override fun onClickNextWalkingPlayer() {
        isWaitingNextPlayer = false

        mGameState?.let { gameState ->
            val nextWalkingPlayer = gameState.walkingPlayer
            val invalidLetterCard = gameState.gameField.lettersField[mLastClickCardPosition]

            changingLiveData.value = ChangingState.CloseInvalidLetter(
                invalidLetterCard
            )

            if (nextWalkingPlayer != null) {
                changingLiveData.value = ChangingState.NextPlayer(
                    nextWalkingPlayer
                )
            }
        }
    }

    override fun onClickNextWord() {
        animalLettersGameInteractor.getNextWordCard()
    }

    override fun onBackPressed() {
        entireLiveData.value = EntireState.IsEndGameState
        gameStopwatch.stop()
        isEndGameDialog = true
    }

    override fun onLoadGame() {
        gameStopwatch.start()
        isEndGameDialog = false
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

        const val ERROR_NEXT_PLAYER_NOT_FOUND = "Следующий игрок не найден"
        const val ERROR_NEXT_GUESSED_WORD_NOT_FOUND = "Следующее загадываемое слово не найдено"
        const val ERROR_INDEX_INCORRECT = "Индекс вышел за пределы корректных значений"
        const val ERROR_INCORRECT_TYPE_OF_APP_STATE = "Некорректный тип AnimalLetterState"
        const val ERROR_NULL_ARRIVED_GAME_STATE = "Обновленное состояние GameState == null "
        const val ERROR_STATE_RESTORE = "Проблема в логике восстановления состояния эерана"


        const val COROUTINE_SCOPE_CANCEL = "in viewModel.onCleared()"
    }
}