package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Avatar(
    val imageName: String,
    val isStandard: Boolean,
    val id: Long = 1
) : Parcelable, DomainEntity {
    companion object {
       const val DEFAULT_IMAGE_NAME = "avatar_cat"
    }
}