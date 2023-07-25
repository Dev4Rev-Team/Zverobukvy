package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.domain.app_state.AnimalLettersChangingState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersEntireState

/**
 Во viewModel необходимо хранить текущее полное состояние игры (var currentGameState: GameState?).
 При получении от интерактора нового полного состояния игры, viewmodel сравнивает текущее состояние
 с новым, формирует и заносит соответствующие данные в сhangingGameStateLiveData и/или
 entireStateLiveData. После этого обновляет текущее полное состояние игры (currentGameState).
 Т.е. во viewModel должна быть реализована логика преобразования данных, полученных от интерактора,
 в данные необходимые для view.
 */
interface IAnimalLettersViewModel {
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
    fun getEntireGameStateLiveData(): LiveData<AnimalLettersEntireState>

    /**
    Методы для подписки view на изменения состояния игры по ходу игры.
    Непосредственно при подписке view ничего получать не будет, т.к. реализовывается подход SingleEvent.
     */
    fun getChangingGameStateLiveData(): SingleEventLiveData<AnimalLettersChangingState>

    /**
    Метод вызывается при выборе буквенной карточки, в этом методе viewModel вызывает
    метод интерактора selectionLetterCard() и после получения ответа присваивает
    сhangingGameStateLiveData изменения состояния игры (реакция на выбор буквенной карточки)
     */
    fun onClickLetterCard(positionSelectedLetterCard: Int)

    /**
    Метод вызывается при нажатии пользователем кнопик Back, в этом методе viewModel (без обращения
    к интерактору) присваивает changingGameStateLiveData состояние, соответствующее открытию
    диалогового окна. При дальнейшем нажатии OK - view вызывает метод viewModel.onEndGameByUser(),
    при нажатии Cancel - view закрывает диалоговое окно и игра продолжается.
     */
    fun onBackPressed()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе viewModel вызывает метод
    интерактора endGameByUser() и после получения ответа, если необходимо присваивает
    сhangingGameStateLiveData текущее состояние игры (если отгадана последняя буква), и после небольшой
    выдержки времени, необходимой для восприятия пользователем отрисованных изменений, присваивает
    entireGameStateLiveData состояние игры - завершение игры.
     */
    fun onEndGameByUser()
}