package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class Player(open var name: String, open var id: Long = 0, open var avatar: Avatar) : Parcelable,
    DomainEntity {

    @Parcelize
    data class HumanPlayer(
        override var name: String,
        override var id: Long = 0,
        override var avatar: Avatar = Avatar.DEFAULT_AVATAR
    ) : Player(name, id, avatar)

    @Parcelize
    object ComputerPlayer : Player("Computer", avatar = Avatar.COMPUTER_AVATAR)

}

