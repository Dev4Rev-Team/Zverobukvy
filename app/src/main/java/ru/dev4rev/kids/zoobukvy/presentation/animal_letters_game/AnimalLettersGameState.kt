package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard

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
         * @param screenDimmingText Текст для затемненного экрана
         */
       class StartGameState(
            val lettersCards: List<LetterCard>,
            val wordCard: WordCard,
            val players: List<PlayerInGame>,
            val nextWalkingPlayer: PlayerInGame,
            val nextWordBtnVisible: Boolean,
            val nextPlayerBtnVisible: Boolean,
            private val screenDimmingText: String
        ) : EntireState

        /** Состояние запроса на прекращение игры, показ диалогового окна
         * (исходит из ViewModel)
         */
        object IsEndGameState : EntireState

        /** Состояние окончания игры
         * (исходит из Interactor)
         *
         * @param players Список игроков
         * @param gameTime Время игры
         * @param isFastEndGame
         * * true - экран со счетам показывать не нужно,
         * * false - стандартный выход из игры
         * @param bestTime - лучшее время и имя игрока
         * * null - еще нет лучшего времени (первая игра), не одиночная игра (с учетом компьютера).
         * @param isRecordTime - установлен или нет новый рекорд времени
         */
        class EndGameState(
            val isFastEndGame : Boolean,
            val players: List<PlayerInGame>,
            val gameTime: String,
            val bestTime: Pair<String, String>?,
            val isRecordTime: Boolean
        ) : EntireState
    }

    sealed interface ChangingState : AnimalLettersGameState {
        /** Состояние верно отгаданной карточки с буквой
         *
         * @param correctLetterCard Карточка, которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
         */
        class CorrectLetter(
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
         * @param screenDimmingText Текст для затемненного экрана
         */
        class InvalidLetter(
            val invalidLetterCard: LetterCard,
            private val screenDimmingText: String,
        ) : ChangingState

        /** Передача хода следующему игроку.
         * Вызывается после [CloseInvalidLetter] и [NextGuessWord], если в игре болеее одного игрока
         *
         * Действия :
         * 1. Передать ход следующему игроку
         *
         * @param nextWalkingPlayer Следующего ходящего игрока
         */
        class NextPlayer(
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
        class CloseInvalidLetter(
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
         * @param correctLetterCard Карточка, которую нужно перевернуть
         * @param positionLetterInWord Отгаданная буква, которую нужно подсветить
         * @param players список игроков (в том числе обновленный счет)
         * @param hasNextWord true - если следующее слово есть, false - если нет
         * @param screenDimmingText Текст для затемненного экрана
         */
        class GuessedWord(
            val correctLetterCard: LetterCard,
            val positionLetterInWord: Int,
            val players: List<PlayerInGame>,
            val hasNextWord: Boolean,
            private val screenDimmingText: String,
        ) : ChangingState

        /** Состояние смены загадываемого слова
         *
         * Подразумевает :
         * 1. Пререворачивание всех ранее открытых карточек рубашаками вверх
         * 2. Очищение подсветки букв в загаданном слове
         * 3. Смену слова
         *
         * @param updatedLettersCards Список всех карточек с буквами (с актуальным цветом букв/звуков)
         * @param wordCard Новое загаданное слово
         */
        class NextGuessWord(
            val updatedLettersCards: List<LetterCard>,
            val wordCard: WordCard
        ) : ChangingState

        /** Состояние обновления карточек-букв при смене режима их озвучки
         * Подразумевает изменение цвета карточек-букв (содержиит список всех карточек на поле)
         */
        class UpdateLettersCards(
            val updatedLettersCards: List <LetterCard>
        ): ChangingState
    }
}