package ru.gb.zverobukvy.domain.app_state

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard

sealed interface AnimalLettersEntireState {
    /** Состояние загрузки/подготовки игры.
     * Дает интерактору время на загрузку карточек из репозитория.
     */
    object LoadingGameState : AnimalLettersEntireState

    /** Полное состояние экрана, передается в случаях:
     * 1. Начала игры
     * 2. При пересоздании View
     *
     * @param lettersCards Список всех карточек с буквами
     * (карточка сама содержит информацию о том, какой стороной она повернута)
     *
     * @param wordCard Слово для отгадывания
     * @param players Список игроков
     * (игрок сам содержит информацию о его счете)
     * @param nextWalkingPlayer Ходящий игрок
     */
    data class StartGameState(
        val lettersCards: List<LetterCard>,
        val wordCard: WordCard,
        val players: List<Player>,
        val nextWalkingPlayer: Player
    ) : AnimalLettersEntireState

    /** Состояние запроса на прекращение игры, показ диалогового окна
     * (исходит из ViewModel)
     */
    object IsEndGameState : AnimalLettersEntireState

    /** Состояние окончания игры
     * (исходит из Interactor)
     *
     * @param players Список игроков
     */
    data class EndGameState(
        val players: List<Player>
    ) : AnimalLettersEntireState
}