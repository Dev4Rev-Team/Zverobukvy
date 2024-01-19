package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.repository.UserFeedbackRepository

interface GameIsOverDialogViewModel {
    fun getPlayersBeforeGame(): List<Player.HumanPlayer>
    fun getPlayersAfterGame(): List<Player.HumanPlayer>
    fun getIsUserFeedback():UserFeedbackRepository
}
