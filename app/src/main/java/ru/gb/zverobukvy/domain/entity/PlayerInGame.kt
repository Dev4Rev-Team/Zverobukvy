package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerInGame(val player: Player, var scoreInCurrentGame: Int = 0): Parcelable
