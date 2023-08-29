package ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.domain.entity.PlayerInGame

@Parcelize
data class PlayerUI(val name: String, var scoreInCurrentGame: Int) : Parcelable {
    companion object {
        fun map(player: PlayerInGame) = PlayerUI(player.name, player.scoreInCurrentGame)
    }
}