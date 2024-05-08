package ru.dev4rev.kids.zoobukvy.domain.entity.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PlayerInGame(val player: Player, var scoreInCurrentGame: Int = 0): Parcelable
