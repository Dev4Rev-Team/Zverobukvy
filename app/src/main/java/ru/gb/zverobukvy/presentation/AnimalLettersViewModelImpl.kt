package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState.ChangingState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState.EntireState
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.entity.LetterCard

import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractor
import java.util.LinkedList


class AnimalLettersViewModelImpl(private val animalLettersInteractor: AnimalLettersInteractor) :
    AnimalLettersViewModel, ViewModel() {

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

            animalLettersInteractor.startGame()

            animalLettersInteractor.subscribeToGameState().collect { newState ->
                val viewState = convert(mGameState, newState)

                viewState.forEachIndexed { index, state ->
                    /** Проверяем возможные ошибки в логике конвертации */
                    if (index > 1)
                        throw IllegalStateException(ERROR_INDEX_INCORRECT)
                    if (index == 1 && state !is EntireState.EndGameState)
                        throw IllegalStateException(ERROR_INCORRECT_TYPE_OF_APP_STATE)


                    if (state is EntireState) {
                        mViewState = state
                        entireLiveData.value = mViewState
                    } else {
                        changingLiveData.value = state as ChangingState
                    }

                    if (index != viewState.lastIndex) delay(STATE_DELAY)
                }

                mGameState = newState
            }
        }
    }

    /** Метод конвертирует разницу состояний (+ [mLastClickCardPosition])
     * в список из одного или двух [AnimalLettersState].
     * * Вторым состоянием может быть только [EntireState.EndGameState]
     */
    private fun convert(oldState: GameState?, newState: GameState?): List<AnimalLettersState> {

        /** Проверка на Null newState :
         * Срабатывает сразу после подписки на данные в Interactor
         */
        if (newState == null) {
            return listOf(EntireState.LoadingGameState)
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
        val stateList = LinkedList<AnimalLettersState>()

        /** Ловим событие окончания игры [EntireState.EndGameState]
         */
        if (!newState.isActive) {
            stateList.addFirst(EntireState.EndGameState(newState.players))
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

            if (nextPlayer != null && nextWord != null) {
                stateList.addFirst(
                    ChangingState.NextGuessWord(
                        newState.gameField.gamingWordCard!!,
                        newState.walkingPlayer!!
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

                stateList.addFirst(ChangingState.InvalidLetter(lastClickCard))
            }

        }

        return stateList
    }

    private fun positionGuessedLetters(oldPositions: List<Int>, newPositions: List<Int>): Int? {
        val position = newPositions.filter { index ->
            !oldPositions.contains(index)
        }.ifEmpty { null }

        return position?.first()
    }

    private fun cardsDiff(
        oldCards: List<LetterCard>,
        newCards: List<LetterCard>,
    ): List<LetterCard>? {
        return newCards.filterIndexed { cardIndex, card ->
            card != oldCards[cardIndex]
        }.ifEmpty { null }
    }


    override fun onActiveGame() {
        if (isEndGameDialog) {
            entireLiveData.value = mViewState
            entireLiveData.value = EntireState.IsEndGameState
        } else {
            entireLiveData.value = mViewState
        }
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
            animalLettersInteractor.selectionLetterCard(positionSelectedLetterCard)
            mLastClickCardPosition = positionSelectedLetterCard
        }

    }

    override fun onClickNextWalkingPlayer() {
        mGameState?.let { gameState ->
            val nextWalkingPlayer = gameState.walkingPlayer
            val invalidLetterCard = gameState.gameField.lettersField[mLastClickCardPosition]

            if (nextWalkingPlayer != null) {
                changingLiveData.value = ChangingState.NextPlayer(
                    nextWalkingPlayer,
                    invalidLetterCard
                )
            } else {
                throw IllegalStateException(ERROR_NEXT_PLAYER_NOT_FOUND)
            }
        }
    }

    override fun onClickNextWord() {
        animalLettersInteractor.getNextWordCard()
    }

    override fun onBackPressed() {
        entireLiveData.value = EntireState.IsEndGameState
        isEndGameDialog = true
    }

    override fun onLoadGame() {
        isEndGameDialog = false
    }

    override fun onEndGameByUser() {
        animalLettersInteractor.endGameByUser()
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


        const val COROUTINE_SCOPE_CANCEL = "in viewModel.onCleared()"
    }
}