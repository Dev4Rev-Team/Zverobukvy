package ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.domain.entity.Player

@Parcelize
data class GameIsOverDialogData(val list: List<PlayerUI>, val time: String = "") : Parcelable {
    companion object {
        fun map(list: List<Player>): List<PlayerUI> {
            return list.map { PlayerUI.map(it) }
        }
    }
}
