package ru.gb.zverobukvy.model.app_state

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

/** Полное состояние экрана
 * Передается в начале игры и после каждого восстановления View
 */
data class SupperState(

    /** Просто список всех карточек/букв */
    val lettersField: List<Char>,
    /** Список номеров перевернутых карточек */
    val flippedLettersCards: List<Int>,

    /** Слово для отгадывания */
    val gamingWord: String,
    /** Текущий счет */
    val scores: List<Player>,
    /** Ходящий игрок */
    val walkingPlayer: Player,
    /** Все подсвечиваемые буквы (Должен знать/хранить интерактор) */
    val positionLetterInWord: List<Int>,
)

/** Передает состояния частей экрана в процессе игры
 */
sealed class PlayerWayState {

    /** Включает в себя:
     * 1. Карточки/карточку котор-ые/ую нужно перевернуть
     * 2. Отгаданную букву которую нужно подсветить
     */
    data class CorrectLetter(
        val flippedLettersCards: List<Int>,
        // или flippedLetterCard: Int
        val positionLetterInWord: Int,
    ) : PlayerWayState()

    /** Включает в себя:
     * 1. Следующего ходящего игрока
     *
     * Подразумевает :
     * 1. Сигнал о том что предыдущая буква отгадана неверно??
     * 2. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 3. Очищение подсветки букв в загаданном слове
     */
    data class InvalidLetter(
        val walkingPlayer: Player,
    ) : PlayerWayState()

    /** Включает в себя:
     * 1. Карточки/карточку котор-ые/ую нужно перевернуть
     * 2. Новое загаданное слово
     * 3. Обновление счета игроков
     *
     * Подразумевает :
     * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 2. Очищение подсветки букв в загаданном слове
     * 3. Сохранение хода за отгадавшим игроком?
     */
    data class GuessedWord(
        val flippedLettersCards: List<Int>,
        // или flippedLetterCard: Int
        val gamingWord: String,
        val scores: List<Player>,
    ) : PlayerWayState()
}