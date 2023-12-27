package ru.dev4rev.kids.zoobukvy.domain.entity.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

@Parcelize
class Avatar(
    val imageName: String,
    val isStandard: Boolean = true,
    var id: Long = 0
) : Parcelable, DomainEntity {
    companion object {
        private const val DEFAULT_IMAGE_NAME = "avatar_cat"
        private const val COMPUTER_IMAGE_NAME = "avatar_bot"
        const val ADD_IMAGE_NAME = "avatar_add"
        private const val DEFAULT_ID = 1L
        val DEFAULT_AVATAR = Avatar(DEFAULT_IMAGE_NAME, true, DEFAULT_ID)
        val COMPUTER_AVATAR = Avatar(COMPUTER_IMAGE_NAME)
        val ADD_AVATAR = Avatar(ADD_IMAGE_NAME)
    }
}