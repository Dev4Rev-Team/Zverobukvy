package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame

@Parcelize
data class PlayerUI(val player: Player, var scoreInCurrentGame: Int) : Parcelable {
    companion object {
        fun map(player: PlayerInGame) = PlayerUI(player.player, player.scoreInCurrentGame)
    }
}