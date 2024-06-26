package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame

@Parcelize
class PlayerUI(val player: Player, var scoreInCurrentGame: Int) : Parcelable {
    companion object {
        fun map(player: PlayerInGame) = PlayerUI(player.player, player.scoreInCurrentGame)
    }
}