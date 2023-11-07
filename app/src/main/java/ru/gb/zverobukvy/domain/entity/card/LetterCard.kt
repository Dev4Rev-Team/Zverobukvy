package ru.gb.zverobukvy.domain.entity.card

import ru.gb.zverobukvy.domain.entity.DomainEntity
import ru.gb.zverobukvy.presentation.customview.CustomCardTable

data class LetterCard(
    override val letter: Char,
    override var isVisible: Boolean = false,
    override val faceImageName: String,
    override val backImageName: String,
    override val soundName: String
) : Card, CustomCardTable.LetterCardUI, DomainEntity