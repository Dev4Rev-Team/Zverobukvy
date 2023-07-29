package ru.gb.zverobukvy.domain.app_state

import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.WordCard

sealed interface AnimalLettersState {

    sealed interface EntireState : AnimalLettersState {
        /** Состояние загрузки/подготовки игры.
         * Дает интерактору время на загрузку карточек из репозитория.
         */
        object LoadingGameState : EntireState

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
            val nextWalkingPlayer: Player,
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
            val players: List<Player>,
        ) : EntireState
    }

    sealed interface ChangingState : AnimalLettersState {
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
         * @see ru.gb.zverobukvy.presentation.AnimalLettersViewModel.onClickNextWord
         *
         * @param correctLetterCard Карточка, которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
         * @param players список игроков (в том числе обновленный счет)
         * @param hasNextWord true - если следующее слово есть, false - если нет
         */
        data class GuessedWord(
            val correctLetterCard: LetterCard,
            val positionLetterInWord: Int,
            val players: List<Player>,
            val hasNextWord: Boolean,
        ) : ChangingState

        /** Состояние смены загадываемого слова
         *
         * Подразумевает :
         * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
         * 2. Очищение подсветки букв в загаданном слове
         * 3. Смену слова
         * 4. Смену игрока
         *
         * @param wordCard Новое загаданное слово
         * @param nextWalkingPlayer Игрок к которому переходит ход
         */
        data class NextGuessWord(
            val wordCard: WordCard,
            val nextWalkingPlayer: Player,
        )
    }
}