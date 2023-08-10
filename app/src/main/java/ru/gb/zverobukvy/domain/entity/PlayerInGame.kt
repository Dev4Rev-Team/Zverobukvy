package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlayerInGame(val name: String, var scoreInCurrentGame: Int = 0): Parcelable
