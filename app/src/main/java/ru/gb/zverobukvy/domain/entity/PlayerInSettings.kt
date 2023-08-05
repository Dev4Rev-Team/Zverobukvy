package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerInSettings(
    val player: Player,
    var isSelectedForGame: Boolean = false,
    var inEditingState: Boolean = false
) : Parcelable
