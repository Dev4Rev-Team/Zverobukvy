package ru.dev4rev.kids.zoobukvy.domain.entity.player

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
class Rating(
    @ColumnInfo(name = "orange_rating")
    var orangeRating: Int = 0,
    @ColumnInfo(name = "green_rating")
    var greenRating: Int = 0,
    @ColumnInfo(name = "blue_rating")
    var blueRating: Int = 0,
    @ColumnInfo(name = "violet_rating")
    var violetRating: Int = 0
): Parcelable
