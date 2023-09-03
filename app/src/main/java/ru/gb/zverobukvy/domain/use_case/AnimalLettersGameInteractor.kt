package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.domain.entity.GameState

/**
 Интерактор хранит и передает во viewModel (через StateFlow) полное состояние игры в виде объекта
 GameState.
 * @exception IllegalArgumentException, если в конструктор переданы не корректные данные
 */
interface AnimalLettersGameInteractor {

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
     * @exception IllegalArgumentException, если из репозитория пришли не корректные данные
     */
    suspend fun startGame()

    /**
    Метод вызывается при выборе буквенной карточки, в этом методе интерактор испускает
    полное текущее состояния игры (реакция на выбор буквенной карточки), в том числе,
    если все слова отгаданы, состояние, соответствующее завершению игры, т.е. isActive = false
     * @param positionSelectedLetterCard позиция выбранной карточки-буквы
     * * @exception IllegalArgumentException, если передана не корректная positionSelectedLetterCard
     */
    fun selectionLetterCard(positionSelectedLetterCard: Int)

    /**
     Метод вызывается после того, как отгадано слово, для получения состояни со следующим
     отгадываемым словом.
     */
    fun getNextWordCard()

    /**
     * Метод вызывается после некорректно выбранной буквенной карточки. Интерактор испускает
     * полное текущее состояние игры с измененным ходящим игроком.
     */
    fun getNextWalkingPlayer()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе интерактор испускает
    полное текущее состояние игры, в котором isActive = false
     */
    fun endGameByUser()
}