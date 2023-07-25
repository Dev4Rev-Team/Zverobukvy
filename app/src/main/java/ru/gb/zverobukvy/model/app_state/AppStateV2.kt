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

sealed interface State {

    /** Супер состояние экрана
     */
    sealed interface SupperState : State {

        /** Состояние загрузки/подготовки игры.
         * Дает интерактору время на загрузку карточек из репозитория.
         */
        object LoadingGameState : SupperState

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
         * @param positionLetterInWord Все подсвечиваемые буквы
         */
        data class StartGameState(
            val lettersCards: List<LetterCard>,
            val wordCard: WordCard,
            val scores: List<Player>,
            val nextWalkingPlayer: Player,
            val positionLetterInWord: List<Int>,
        ) : SupperState

        /** Состояние запроса на прекращение игры, показ диалогового окна
         * (исходит из ViewModel)
         */
        object IsEndGameState : SupperState

        /** Отмена состояния [IsEndGameState], закрытие диалогового окна о закрытии игры
         * (исходит из ViewModel)
         */
        object IsLoadGameState : SupperState

        /** Состояние окончания игры
         * (исходит из Interactor)
         *
         * @param scores Итоговый счет
         */
        data class EndGameState(
            val scores: List<Player>,
        ) : SupperState
    }

    /** Передает состояния частей экрана в процессе игры
     */
    sealed class PlayerWayState : State {

        /** Состояние верно отгаданной карточки с буквой
         *
         * @param flippedLetterCard Карточка которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква которую нужно подсветить
         */
        data class CorrectLetter(
            val flippedLetterCard: LetterCard,
            val positionLetterInWord: Int,
        ) : PlayerWayState()

        /** Состояния неверно выбранной карточки
         *
         * Действия :
         * 1. Перевернуть выбранную карточку рубашкой вниз
         * 2. Сообщить пользователю что карточка неверная
         * 3. Перевернуть карточку обратно
         *
         * @param nextWalkingPlayer Следующего ходящего игрока
         * @param invalidLetterCard Невалидную карточку
         */
        data class InvalidLetter(
            val nextWalkingPlayer: Player,
            val invalidLetterCard: LetterCard,
        ) : PlayerWayState()

        /** Состояние успешно отгаданного слова.
         * Посылается после отправки [CorrectLetter] (?с задержкой?)
         *
         * Подразумевает :
         * 1. Смену загадываемого слова
         * 2. Пререворачивание всех ранее открытых карточек рубашаками вверх
         * 3. Очищение подсветки букв в загаданном слове
         * 4. Смену игрока
         *
         * @param positionLetterInWord Позиция отгаданной буквы в слове
         * @param wordCard Новое загаданное слово
         * @param nextWalkingPlayer Игрок к которому переходит ход
         * @param scores Обновленный счет игроков
         */
        data class GuessedWord(
            val positionLetterInWord: Int,
            val wordCard: WordCard?,
            val nextWalkingPlayer: Player?,
            val scores: List<Player>,
        ) : PlayerWayState()
    }
}