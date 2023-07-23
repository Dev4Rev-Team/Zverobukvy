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
sealed interface SupperState {

    /** Полное состояние экрана, передается в случаях:
     * 1. Начла игры
     * 2. При пересоздании View
     */
    data class StartGameState(

        /** Список всех карточек с буквами */
        val lettersCards: List<LetterCard>,

        /** Слово для отгадывания */
        val wordCard: WordCard,
        /** Текущий счет */
        val scores: List<Player>,
        /** Ходящий игрок */
        val walkingPlayer: Player,
        /** Все подсвечиваемые буквы (Должен знать/хранить интерактор) */
        val positionLetterInWord: List<Int>,
    ) : SupperState

    /** Состояние окончания игры, если закончились загадываемые слова
     * (исходит из Interactor)
     */
    data class GuessedWordEndGame(
        val flippedLetterCard: LetterCard,
        val positionLetterInWord: Int,
        val scores: List<Player>,
    ) : SupperState

    /** Состояние запроса на прекращение игры
     * (исходит из ViewModel)
     */
    object IsEndGame : SupperState

    /** Состояние окончания игры, если пользователь нажал Back
     * (исходит из Interactor)
     */
    data class BackPressedEndGame(
        val scores: List<Player>,
    ) : SupperState
}

/** Передает состояния частей экрана в процессе игры
 */
sealed class PlayerWayState {

    /** Включает в себя:
     * 1. Карточку которую нужно перевернуть
     * 2. Отгаданную букву которую нужно подсветить
     */
    data class CorrectLetter(
        val flippedLetterCard: LetterCard,
        val positionLetterInWord: Int,
    ) : PlayerWayState()

    /** Включает в себя:
     * 1. Следующего ходящего игрока
     * 2. Невалидную карточку
     *
     * Подразумевает :
     * 1. Сигнал о том что предыдущая буква отгадана неверно??
     * 2. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 3. Очищение подсветки букв в загаданном слове
     */
    data class InvalidLetter(
        val walkingPlayer: Player,
        val invalidLetterCard: LetterCard,
    ) : PlayerWayState()

    /** Включает в себя:
     * 1. Карточку которую нужно перевернуть
     * 2. Отгаданную букву которую нужно подсветить
     * 3. Новое загаданное слово
     * 4. Обновленный счет игроков
     *
     * Подразумевает :
     * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 2. Очищение подсветки букв в загаданном слове
     * 3. Сохранение хода за отгадавшим игроком?
     */
    data class GuessedWord(
        val flippedLetterCard: LetterCard,
        val positionLetterInWord: Int,
        val wordCard: WordCard,
        val scores: List<Player>,
    ) : PlayerWayState()
}