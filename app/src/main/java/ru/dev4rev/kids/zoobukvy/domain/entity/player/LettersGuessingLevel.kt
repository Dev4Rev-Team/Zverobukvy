package ru.dev4rev.kids.zoobukvy.domain.entity.player

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
class LettersGuessingLevel(
    @ColumnInfo(name = "orange_level")
    var orangeLevel: Float = DEFAULT_ORANGE_LEVEL,
    @ColumnInfo(name = "green_level")
    var greenLevel: Float = DEFAULT_GREEN_LEVEL,
    @ColumnInfo(name = "blue_level")
    var blueLevel: Float = DEFAULT_BLUE_LEVEL,
    @ColumnInfo(name = "violet_level")
    var violetLevel: Float = DEFAULT_VIOLET_LEVEL
): Parcelable{
    companion object {
        private const val DEFAULT_ORANGE_LEVEL = 0.111F
        private const val DEFAULT_GREEN_LEVEL = 0.083F
        private const val DEFAULT_BLUE_LEVEL = 0.062F
        private const val DEFAULT_VIOLET_LEVEL = 0.05F
    }
}
