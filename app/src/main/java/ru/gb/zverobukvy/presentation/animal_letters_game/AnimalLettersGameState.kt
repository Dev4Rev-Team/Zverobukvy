package ru.gb.zverobukvy.presentation.animal_letters_game

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.WordCard

sealed interface AnimalLettersGameState {

    sealed interface EntireState : AnimalLettersGameState {

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
         * @param nextWordBtnVisible true - Показать кнопку с переходом к следующему слову
         * @param nextPlayerBtnVisible true - показать кнопку о переходе к следующему игроку
         */
        data class StartGameState(
            val lettersCards: List<LetterCard>,
            val wordCard: WordCard,
            val players: List<PlayerInGame>,
            val nextWalkingPlayer: PlayerInGame,
            val nextWordBtnVisible: Boolean,
            val nextPlayerBtnVisible: Boolean,
        ) : EntireState

        /** Состояние запроса на прекращение игры, показ диалогового окна
         * (исходит из ViewModel)
         */
        object IsEndGameState : EntireState

        /** Состояние окончания игры
         * (исходит из Interactor)
         *
         * @param players Список игроков
         */
        data class EndGameState(
            val players: List<PlayerInGame>,
            val gameTime: String,
        ) : EntireState
    }

    sealed interface ChangingState : AnimalLettersGameState {
        /** Состояние верно отгаданной карточки с буквой
         *
         * @param correctLetterCard Карточка, которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
         */
        data class CorrectLetter(
            val correctLetterCard: LetterCard,
            val positionLetterInWord: Int,
        ) : ChangingState

        /** Состояния неверно выбранной карточки
         *
         * Действия :
         * 1. Перевернуть выбранную карточку рубашкой вниз
         * 2. Сообщить пользователю, что карточка неверная
         *
         * @param invalidLetterCard Невалидную карточку
         */
        data class InvalidLetter(
            val invalidLetterCard: LetterCard,
        ) : ChangingState

        /** Передача хода следующему игроку.
         * Вызывается после [CloseInvalidLetter] и [NextGuessWord], если в игре болеее одного игрока
         *
         * Действия :
         * 1. Передать ход следующему игроку
         *
         * @param nextWalkingPlayer Следующего ходящего игрока
         */
        data class NextPlayer(
            val nextWalkingPlayer: PlayerInGame,
        ) : ChangingState

        /** Команда к перевороту неверной карточки.
         * Вызывается после [InvalidLetter]
         *
         * Действия :
         * 1. Перевернуть карточку обратно
         *
         * @param invalidLetterCard Невалидную карточку
         */
        data class CloseInvalidLetter(
            val invalidLetterCard: LetterCard,
        ) : ChangingState

        /** Состояние успешно отгаданного слова.
         * Посылается ВМЕСТО отправки [CorrectLetter]
         *
         * Подразумевает :
         * 1. Действий идентичных выполняемым при состоянии [CorrectLetter]
         * 2. Уведомление игроков о том что слово отгадано?
         * 3. Смену счета
         * 4. В зависимости от [hasNextWord] :
         *  * Если [hasNextWord] == true, выводим диалог/кнопку с предложением сменить
         * слово и игрока. При нажатии на кнопку приходит состояние [NextGuessWord]
         *  * Если [hasNextWord] == false, следом приходит состояние [EntireState.EndGameState]
         *
         * @see ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModel.onClickNextWord
         *
         * @param correctLetterCard Карточка, которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
         * @param players список игроков (в том числе обновленный счет)
         * @param hasNextWord true - если следующее слово есть, false - если нет
         */
        data class GuessedWord(
            val correctLetterCard: LetterCard,
            val positionLetterInWord: Int,
            val players: List<PlayerInGame>,
            val hasNextWord: Boolean,
        ) : ChangingState

        /** Состояние смены загадываемого слова
         *
         * Подразумевает :
         * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
         * 2. Очищение подсветки букв в загаданном слове
         * 3. Смену слова
         *
         * @param wordCard Новое загаданное слово
         */
        data class NextGuessWord(
            val wordCard: WordCard,
        ) : ChangingState
    }
}