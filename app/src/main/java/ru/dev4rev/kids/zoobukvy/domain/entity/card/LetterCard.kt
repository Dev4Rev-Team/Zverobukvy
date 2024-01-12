package ru.dev4rev.kids.zoobukvy.domain.entity.card

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity
import ru.dev4rev.kids.zoobukvy.domain.use_case.color_letters.ColorLetterEnum
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomCardTable

data class LetterCard(
    override val letter: Char,
    override var isVisible: Boolean = false,
    override val faceImageName: String,
    override val backImageName: String,
    override val soundName: String,
    override val letterName: String,
    override var colorLetterEnum: ColorLetterEnum = ColorLetterEnum.BLACK
) : Card, CustomCardTable.LetterCardUI, DomainEntity