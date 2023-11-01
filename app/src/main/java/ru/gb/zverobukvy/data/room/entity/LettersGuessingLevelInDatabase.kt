package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo

data class LettersGuessingLevelInDatabase(
    @field:ColumnInfo(name = "orange_correct")
    var orangeCorrect:Int,
    @field:ColumnInfo(name = "orange_invalid")
    var orangeInvalid:Int,
    @field:ColumnInfo(name = "green_correct")
    var greenCorrect:Int,
    @field:ColumnInfo(name = "green_invalid")
    var greenInvalid:Int,
    @field:ColumnInfo(name = "blue_correct")
    var blueCorrect:Int,
    @field:ColumnInfo(name = "blue_invalid")
    var blueInvalid:Int,
    @field:ColumnInfo(name = "violet_correct")
    var violetCorrect:Int,
    @field:ColumnInfo(name = "violet_invalid")
    var violetInvalid:Int
)
