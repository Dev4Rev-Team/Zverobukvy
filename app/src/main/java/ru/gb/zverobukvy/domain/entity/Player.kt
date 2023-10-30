package ru.gb.zverobukvy.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
sealed class Player(
    open var name: String,
    open var id: Long = 0,
    open var avatar: Avatar,
    open var rating: Rating = Rating(),
    open var lettersGuessingLevel: LettersGuessingLevel = LettersGuessingLevel()
) : Parcelable,
    DomainEntity {

    @Parcelize
    data class HumanPlayer(
        override var name: String,
        override var id: Long = 0,
        override var avatar: Avatar = Avatar.DEFAULT_AVATAR,
        override var rating: Rating = Rating(),
        override var lettersGuessingLevel: LettersGuessingLevel = LettersGuessingLevel()
    ) : Player(name, id, avatar, rating, lettersGuessingLevel)

    @Parcelize
    object ComputerPlayer : Player(COMPUTER_NAME, avatar = Avatar.COMPUTER_AVATAR)

    companion object {
        private const val COMPUTER_NAME = "Computer"
    }
}

