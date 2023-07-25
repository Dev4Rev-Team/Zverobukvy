package ru.gb.zverobukvy.model.app_state

import ru.gb.zverobukvy.model.dto.LetterCard
import ru.gb.zverobukvy.model.dto.WordCard

/** Класс игрока просто для примера
 */
sealed class Player(
    open val name: String,
    open val gameScore: Int,
    open val color: Int,
) {

    class RealPersonPlayer(
        override val name: String,
        override val gameScore: Int,
        override val color: Int,
    ) : Player(name, gameScore, color)

    class ComputerPlayer(
        override val name: String,
        override val gameScore: Int,
        override val color: Int,
    ) : Player(name, gameScore, color)
}


/** Супер состояние экрана
 */
sealed interface AnimalLettersEntireState {

    /** Состояние загрузки/подготовки игры.
     * Дает интерактору время на загрузку карточек из репозитория.
     */
    object LoadingGameState : AnimalLettersEntireState

    /** Полное состояние экрана, передается в случаях:
     * 1. Начла игры
     * 2. При пересоздании View
     *
     * @param lettersCards Список всех карточек с буквами
     * (карточка сама содержит информацию о том, какой стороной она повернута)
     *
     * @param wordCard Слово для отгадывания
     * @param scores Текущий счет
     * @param nextWalkingPlayer Ходящий игрок
     */
    data class StartGameState(
        val lettersCards: List<LetterCard>,
        val wordCard: WordCard,
        val scores: List<Player>,
        val nextWalkingPlayer: Player,
    ) : AnimalLettersEntireState

    /** Состояние запроса на прекращение игры, показ диалогового окна
     * (исходит из ViewModel)
     */
    object IsEndGameState : AnimalLettersEntireState

    /** Состояние окончания игры
     * (исходит из Interactor)
     *
     * @param scores Итоговый счет
     */
    data class EndGameState(
        val scores: List<Player>,
    ) : AnimalLettersEntireState
}

/** Передает состояния частей экрана в процессе игры
 */
sealed class AnimalLettersChangingState {

    /** Состояние верно отгаданной карточки с буквой
     *
     * @param correctLetterCard Карточка которую нужно перевернуть
     * @param positionLetterInWord Отгаданная буква которую нужно подсветить
     */
    data class CorrectLetter(
        val correctLetterCard: LetterCard,
        val positionLetterInWord: Int,
    ) : AnimalLettersChangingState()

    /** Состояния неверно выбранной карточки
     *
     * Действия :
     * 1. Перевернуть выбранную карточку рубашкой вниз
     * 2. Сообщить пользователю что карточка неверная
     *
     * @param invalidLetterCard Невалидную карточку
     */
    data class InvalidLetter(
        val invalidLetterCard: LetterCard,
    ) : AnimalLettersChangingState()

    /** Передача хода следующему игроку.
     * Вызывается после [InvalidLetter]
     *
     * Действия :
     * 1. Перевернуть карточку обратно
     * 2. Передать ход следующему игроку
     *
     * @param nextWalkingPlayer Следующего ходящего игрока
     * @param invalidLetterCard Невалидную карточку
     */
    data class NextPlayer(
        val nextWalkingPlayer: Player,
        val invalidLetterCard: LetterCard,
    ) : AnimalLettersChangingState()

    /** Состояние успешно отгаданного слова.
     * Посылается после отправки [CorrectLetter] (?с задержкой?)
     *
     * Подразумевает :
     * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 2. Очищение подсветки букв в загаданном слове
     * 3. Смену игрока
     *
     * @param wordCard Новое загаданное слово
     * @param nextWalkingPlayer Игрок к которому переходит ход
     * @param players Обновленный счет игроков
     */
    data class GuessedWord(
        val wordCard: WordCard?,
        val nextWalkingPlayer: Player?,
        val players: List<Player>,
    ) : AnimalLettersChangingState()
}