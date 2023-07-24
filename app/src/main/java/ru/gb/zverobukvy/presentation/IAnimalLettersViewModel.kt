package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.domain.app_state.AnimalLettersChangingState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersEntireState

interface IAnimalLettersViewModel {
    /**
    Метод вызывается при создании или при пересоздании view, в этом методе viewModel присваивает
    entireStateLiveData полное состояние игры (все данные для полной отрисовки экрана игры).
    Метод вызывается до подписки на entireStateLiveData
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
    Метод вызывается при выборе буквенной карточке, в этом методе viewModel присваивает
    сhangingGameStateLiveData изменения состояния игры (реакция на выбор буквенной карточки)
     */
    fun onClickLetterCard(positionSelectedLetterCard: Int)

    /**
    Метод вызывается при завершении игры пользователем, в этом методе viewModel присваивает
    сhangingGameStateLiveData полное состояние игры (конец игры)
     */
    fun onEndGameByUser()
}