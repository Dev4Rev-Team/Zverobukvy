package ru.gb.zverobukvy.data.room.entity

import androidx.room.ColumnInfo

data class ColorCards (
    @field:ColumnInfo(name = "orange")
    val isOrange: Boolean,
    @field:ColumnInfo(name = "green")
    val isGreen: Boolean,
    @field:ColumnInfo(name = "blue")
    val isBlue: Boolean,
    @field:ColumnInfo(name = "violet")
    val isViolet: Boolean
)