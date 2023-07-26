package ru.gb.zverobukvy.domain.app_state

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard

sealed interface AnimalLettersChangingState {
    /** Состояние верно отгаданной карточки с буквой
     *
     * @param correctLetterCard Карточка, которую нужно перевернуть
     * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
     */
    data class CorrectLetter(
        val correctLetterCard: LetterCard,
        val positionLetterInWord: Int
    ) : AnimalLettersChangingState

    /** Состояния неверно выбранной карточки
     *
     * Действия :
     * 1. Перевернуть выбранную карточку рубашкой вниз
     * 2. Сообщить пользователю, что карточка неверная
     *
     * @param invalidLetterCard Невалидную карточку
     */
    data class InvalidLetter(
        val invalidLetterCard: LetterCard
    ) : AnimalLettersChangingState

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
        val invalidLetterCard: LetterCard
    ) : AnimalLettersChangingState

    /** Состояние успешно отгаданного слова.
     * Посылается после отправки [CorrectLetter] (с задержкой)
     *
     * Подразумевает :
     * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
     * 2. Очищение подсветки букв в загаданном слове
     * 3. Смену игрока
     *
     * @param wordCard Новое загаданное слово
     * @param nextWalkingPlayer Игрок к которому переходит ход
     * @param players список угроков (в том числе обновленный счет)
     */
    data class GuessedWord(
        val wordCard: WordCard?,
        val nextWalkingPlayer: Player?,
        val players: List<Player>
    ) : AnimalLettersChangingState
}
