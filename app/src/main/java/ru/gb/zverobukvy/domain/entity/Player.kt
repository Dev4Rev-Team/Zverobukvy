package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Player(
    var name: String,
    val id: Long = 1,
    val avatar: Avatar = Avatar("avatar_cat", true)
) : Parcelable, DomainEntity
