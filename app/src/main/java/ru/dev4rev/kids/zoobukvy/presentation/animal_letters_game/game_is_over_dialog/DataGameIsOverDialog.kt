package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame

@Parcelize
class DataGameIsOverDialog(val list: List<PlayerUI>, val time: String = "", val bestTime: Pair<String, String>?, val isRecordTime: Boolean) : Parcelable {
    companion object {
        fun map(list: List<PlayerInGame>): List<PlayerUI> {
            return list.map { PlayerUI.map(it) }
        }
    }
}
