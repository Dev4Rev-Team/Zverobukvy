package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerInSettings(
    val name: String,
    var isSelectedForGame: Boolean = false,
    var inEditingState: Boolean = false
) : Parcelable
