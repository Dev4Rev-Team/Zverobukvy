package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.domain.app_state.AnimalLettersEntireState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersChangingState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards

interface IAnimalLettersInteractor {
    /**
    При создании интерактора в конструктор надо передать список выбранного уровня игры (цвета игры)
    и список игроков
     */
    val typesCards: List<TypeCards>

    val players: List<Player>

    /**
    Метод для подписки viewModel на полное состояние игры.
    Предполагается, что viewModel вызывает этот метод в блоке init {}. Т.к. StateFlow при создании
    должен быть заполнен, каким-то начальным состоянием, то при непосредственной подписке viwModel получит это состояние.
    Это будет полное состояние игры Loading.

     */
    fun subscribeToEntireGameState(): StateFlow<AnimalLettersEntireState>

    /**
    Методы для подписки viewModel на изменения состояния игры по ходу игры.
    Предполагается, что viewModel вызывает этот метод в блоке init {}. Т.к. StateFlow при создании
    должен быть заполнен, каким-то начальным состоянием, то при подписке viwModel получит это состояние.
    Предполагается, что это будет какое-то "пустое" состояние изменения игры,
    по которому во view ничего изменять не надо.
     */
    fun subscribeToChangingGameState(): StateFlow<AnimalLettersChangingState>

    /**
    Метод вызывается при создании или при пересоздании view, в этом методе интерактор испускает
    полное состояние игры (все данные для полной отрисовки экрана игры)
     */
    fun activeGameState()

    /**
    Метод вызывается при выборе буквенной карточке, в этом методе интерактор испускает
    изменения состояния игры (реакция на выбор буквенной карточки)
     */
    fun selectionLetterCard()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе интерактор испускает
    полное состояние игры (конец игры)
     */
    fun endGameByUser()
}