package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class LettersGuessingLevel(
    @ColumnInfo(name = "orange_level")
    var orangeLevel: Float = 0F,
    @ColumnInfo(name = "green_level")
    var greenLevel: Float = 0F,
    @ColumnInfo(name = "blue_level")
    var blueLevel: Float = 0F,
    @ColumnInfo(name = "violet_level")
    var violetLevel: Float = 0F
): Parcelable
