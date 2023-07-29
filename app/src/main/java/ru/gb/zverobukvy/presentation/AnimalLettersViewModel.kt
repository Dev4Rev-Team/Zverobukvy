package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState

/**
Во viewModel необходимо хранить текущее полное состояние игры (var currentGameState: GameState?).
При получении от интерактора нового полного состояния игры, viewmodel сравнивает текущее состояние
с новым, формирует и заносит соответствующие данные в сhangingGameStateLiveData и/или
entireStateLiveData. После этого обновляет текущее полное состояние игры (currentGameState).
Т.е. во viewModel должна быть реализована логика преобразования данных, полученных от интерактора,
в данные необходимые для view.
 */
interface AnimalLettersViewModel {
    /**
    Метод вызывается при создании или при пересоздании view, в этом методе viewModel присваивает
    entireStateLiveData полное состояние игры (все данные для полной отрисовки экрана игры).
     */
    fun onActiveGame()

    /**
    Метод для подписки view на полное состояние игры.
    Непосредственно при подписке view получает Loading (при первом создании) или текущее полное состояние игры
    (при пересоздании).
     */
    fun getEntireGameStateLiveData(): LiveData<AnimalLettersState.EntireState>

    /**
    Методы для подписки view на изменения состояния игры по ходу игры.
    Непосредственно при подписке view ничего получать не будет, т.к. реализовывается подход SingleEvent.
     */
    fun getChangingGameStateLiveData(): LiveData<AnimalLettersState.ChangingState>

    /**
    Метод вызывается при выборе буквенной карточки, в этом методе viewModel вызывает
    метод интерактора selectionLetterCard() и после получения ответа присваивает
    сhangingGameStateLiveData изменения состояния игры (реакция на выбор буквенной карточки)
     */
    fun onClickLetterCard(positionSelectedLetterCard: Int)

    /**
    Метод вызывается при нажатии пользователем кнопки перехода хода к следующему игроку, в этом методе
    viewModel (без обращения к интерактору) присваивает сhangingGameStateLiveData изменение
    состояния игры, соответствующее переходу хода к следующему игроку
     */
    fun onClickNextWalkingPlayer()

    /**
    Метод вызывается при нажатии кнопки перехода к следующему слову

     * @see [ru.gb.zverobukvy.domain.app_state.AnimalLettersState.ChangingState.NextGuessWord]
     */
    fun onClickNextWord()

    /**
    Метод вызывается при нажатии пользователем кнопик Back, в этом методе viewModel (без обращения
    к интерактору) присваивает changingGameStateLiveData состояние, соответствующее открытию
    диалогового окна. При дальнейшем нажатии OK - view вызывает метод viewModel.onEndGameByUser(),
    при нажатии Cancel - view закрывает диалоговое окно и уведомляет об этом viewModel
    посредством вызова [onLoadGame].
     */
    fun onBackPressed()

    /**
    Метод вызывается при закрытии диалогового окна о прекращении игры пользователем, никаких
    новых состояний viewModel не отправляет.
     */
    fun onLoadGame()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе viewModel вызывает метод
    интерактора endGameByUser() и после получения ответа присваивает
    entireGameStateLiveData состояние игры - завершение игры.
     */
    fun onEndGameByUser()
}