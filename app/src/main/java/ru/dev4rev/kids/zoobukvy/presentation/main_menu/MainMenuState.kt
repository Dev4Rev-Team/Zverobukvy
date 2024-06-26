package ru.dev4rev.kids.zoobukvy.presentation.main_menu

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

sealed interface MainMenuState {

    sealed interface PlayersScreenState {

        /**
         * Состояние для отрисовки всего списка игроков: при создании или пересоздании view.
         */
        class PlayersState(
            val playersInSettings: List<PlayerInSettings?>
        ) : PlayersScreenState

        /**
         * Состояние для добавления item нового игрока
         */
        class AddPlayerState(
            val playersInSettings: List<PlayerInSettings?>,
            val positionAddPlayer: Int
        ) : PlayersScreenState

        /**
         * Состояние для удаления игрока
         */
        class RemovePlayerState(
            val playersInSettings: List<PlayerInSettings?>,
            val positionRemovePlayer: Int
        ) : PlayersScreenState

        /**
         * Состояние для изменения item игрока, используется в следующих случаях:
         * - пользователем изменено состояние участия игрока в игре (isSelectedForGame);
         * - при запросе пользователя на изменение данных игрока: изменяется режим item игрока
         * (inEditingState=true);
         * - при непосредственном изменении пользователем данных игрока: изменяются данные игрока
         * и режим item игрока (inEditingState=false);
         * - при отказе пользователя на изменение данных игрока: изменяется режим item игрока
         * (inEditingState=false).
         */
        class ChangedPlayerState(
            val playersInSettings: List<PlayerInSettings?>,
            val positionChangedPlayer: Int
        ) : PlayersScreenState
    }

    sealed interface ScreenState {

        /**
         * Состояние для первичной (при создании и восстановлении View) отрисовки выбранного
         * уровня игры (цвета игры)
         */
        class TypesCardsState(
            val typesCard: List<TypeCards>
        ): ScreenState

        /**
         * Состояние для информационных сообщений:
         * - некорректное или повторяющееся имя игрока при редактировании данных игрока;
         * - не выбраны игроки или выбрано слишком много игроков для игры;
         * - не выбраны уровни игры
         */
        class ErrorState(
            val error: String
        ): ScreenState

        /**
         * Состояние для запуска игры "Зверобуквы"
         */
        class StartGame(
            val typesCardsSelectedForGame: List<TypeCards>,
            val playersSelectedForGame: List<PlayerInGame>
        ) : ScreenState

        /**
         * Состояние для закрытия приложения
         */
        object CloseAppState: ScreenState
    }

    /**
     * Состояния для вывода на экран инструкции к игре
     */
    object ShowInstructionsScreenState

    sealed interface AvatarsScreenState{
        /**
         * Состояние, по которому отображается список аватарок для выбора пользователем.
         */
        class ShowAvatarsState(
            val avatars: List<Avatar>,
            val scrollPosition: Int = 0
        ): AvatarsScreenState

        /**
         * Состояние, по которому скрывается список аватарок.
         */
        object HideAvatarsState: AvatarsScreenState
    }
}