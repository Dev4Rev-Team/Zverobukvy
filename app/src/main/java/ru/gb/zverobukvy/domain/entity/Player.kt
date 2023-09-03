package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    var name: String,
    val id: Long = 0,
    var avatar: Avatar = Avatar.DEFAULT_AVATAR
) : Parcelable, DomainEntity
