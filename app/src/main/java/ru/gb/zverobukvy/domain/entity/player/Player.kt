package ru.gb.zverobukvy.domain.entity.player

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.domain.entity.DomainEntity


@Parcelize
sealed class Player(
    open var name: String,
    open var id: Long = 0,
    open var avatar: Avatar,
    open val rating: Rating = Rating(),// кол-во отгаданных слов по цветам
    open val lettersGuessingLevel: LettersGuessingLevel = LettersGuessingLevel() // доля угаданных букв по цветам
) : Parcelable,
    DomainEntity {

    @Parcelize
    data class HumanPlayer(
        override var name: String,
        override var id: Long = 0,
        override var avatar: Avatar = Avatar.DEFAULT_AVATAR,
        override val rating: Rating = Rating(),
        override val lettersGuessingLevel: LettersGuessingLevel = LettersGuessingLevel()
    ) : Player(name, id, avatar, rating, lettersGuessingLevel)

    @Parcelize
    object ComputerPlayer : Player(COMPUTER_NAME, avatar = Avatar.COMPUTER_AVATAR)

    companion object {
        private const val COMPUTER_NAME = "Computer"
    }
}

