package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.domain.entity.GameState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards

/**
 Интерактор хранит и передает во viewModel (через StateFlow) полное состояние игры в виде объекта
 GameState.
 */
interface IAnimalLettersInteractor {
    /**
    При создании интерактора в конструктор надо передать список выбранного уровня игры (цвета игры)
    и список игроков
     */
    val typesCards: List<TypeCards>

    val players: List<Player>

    /**
    Метод для подписки viewModel на состояние игры.
    Предполагается, что viewModel вызывает этот метод в блоке init {}. При непосредственной подписке
    viwModel получит состояние null, что должно соответствовать состоянию игры Loading. Далее после загрузки
    данных из БД viewModel получит от интерактора начальное состояние игры, а по ходу игры будет получать
    текущие полные состояния игры.
     */
    fun subscribeToGameState(): StateFlow<GameState?>

    /**
    Метод вызывается один раз при создании view, в этом методе интерактор, после обращения в БД,
    испускает начальное состояние игры (все данные для полной отрисовки экрана игры)
     */
    fun startGame()

    /**
    Метод вызывается при выборе буквенной карточки, в этом методе интерактор испускает
    полное текущее состояния игры (реакция на выбор буквенной карточки), в том числе,
    если все слова отгаданы, состояние, соответствующее завершению игры, т.е. isActive = false
     */
    fun selectionLetterCard()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе интерактор испускает
    полное текущее состояние игры, в котором isActive = false
     */
    fun endGameByUser()
}