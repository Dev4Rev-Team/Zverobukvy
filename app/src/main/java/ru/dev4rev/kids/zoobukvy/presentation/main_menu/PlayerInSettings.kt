package ru.dev4rev.kids.zoobukvy.presentation.main_menu

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player

@Parcelize
data class PlayerInSettings(
    val player: Player,
    var isSelectedForGame: Boolean = false,
    var inEditingState: Boolean = false
) : Parcelable
