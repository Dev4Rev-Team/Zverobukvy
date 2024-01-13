package ru.dev4rev.kids.zoobukvy.domain.entity.card

import ru.dev4rev.kids.zoobukvy.data.room.entity.card.LettersColor
import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomCardTable

data class LetterCard(
    override val letter: Char,
    override var isVisible: Boolean = false,
    override val faceImageName: String,
    override val backImageName: String,
    override var soundName: String,
    override val letterName: String,
    val baseSoundName: String,
    val softSoundName: String?,
    override var color: LettersColor = LettersColor.Black
) : Card, CustomCardTable.LetterCardUI, DomainEntity