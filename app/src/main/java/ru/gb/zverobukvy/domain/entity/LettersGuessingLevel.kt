package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LettersGuessingLevel(
    var orangeLevel: Pair<Int, Int> = 0 to 0, // first - угаданные буквы, second - все буквы
    var greenLevel: Pair<Int, Int> = 0 to 0,
    var blueLevel: Pair<Int, Int> = 0 to 0,
    var violetLevel: Pair<Int, Int> = 0 to 0
): Parcelable
