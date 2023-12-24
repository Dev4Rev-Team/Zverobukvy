package ru.dev4rev.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import ru.dev4rev.zoobukvy.domain.entity.player.Player

interface GameIsOverDialogViewModel {
    fun getPlayersBeforeGame(): List<Player.HumanPlayer>
    fun getPlayersAfterGame(): List<Player.HumanPlayer>
}