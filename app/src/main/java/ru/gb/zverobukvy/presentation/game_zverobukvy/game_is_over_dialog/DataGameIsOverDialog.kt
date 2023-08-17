package ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.domain.entity.PlayerInGame

@Parcelize
data class DataGameIsOverDialog(val list: List<PlayerUI>, val time: String = "") : Parcelable {
    companion object {
        fun map(list: List<PlayerInGame>): List<PlayerUI> {
            return list.map { PlayerUI.map(it) }
        }
    }
}