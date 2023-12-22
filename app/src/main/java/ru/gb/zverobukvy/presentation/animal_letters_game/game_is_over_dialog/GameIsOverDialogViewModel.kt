package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import ru.gb.zverobukvy.domain.entity.player.Player

interface GameIsOverDialogViewModel {
    fun getPlayersBeforeGame(): List<Player.HumanPlayer>
    fun getPlayersAfterGame(): List<Player.HumanPlayer>
}