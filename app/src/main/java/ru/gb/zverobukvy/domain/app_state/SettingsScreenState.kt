package ru.gb.zverobukvy.domain.app_state

import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.domain.entity.TypeCards

sealed interface SettingsScreenState {

    sealed interface PlayersScreenState {

        /**
         * Состояние для отрисовки всего списка игроков: при создании или пересоздании view.
         */
        data class PlayersState(
            val playersInSettings: List<PlayerInSettings>
        ) : PlayersScreenState

        /**
         * Состояние для добавления item нового игрока
         */
        data class AddPlayerState(
            val playersInSettings: List<PlayerInSettings>,
            val positionAddPlayer: Int
        ) : PlayersScreenState

        /**
         * Состояние для удаления игрока
         */
        data class RemovePlayerState(
            val playersInSettings: List<PlayerInSettings>,
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
        data class ChangedPlayerState(
            val playersInSettings: List<PlayerInSettings>,
            val positionChangedPlayer: Int
        ) : PlayersScreenState
    }

    sealed interface ScreenState {

        /**
         * Состояние для информационных сообщений:
         * - некорректное или повторяющееся имя игрока при редактировании данных игрока;
         * - не выбраны игроки или выбрано слишком много игроков для игры;
         * - не выбраны уровни игры.
         */
        data class ErrorState(
            val error: String
        ): ScreenState

        /**
         * Состояние для запуска игры "Зверобуквы"
         */
        data class StartGame(
            val typesCardsSelectedForGame: List<TypeCards>,
            val playersSelectedForGame: List<PlayerInGame>
        ) : ScreenState
    }
}