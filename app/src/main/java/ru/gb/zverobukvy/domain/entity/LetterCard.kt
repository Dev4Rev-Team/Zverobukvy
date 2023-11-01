package ru.gb.zverobukvy.domain.entity

import ru.gb.zverobukvy.presentation.customview.CustomCardTable

data class LetterCard(
    override val letter: Char,
    override val typesCards: List<TypeCards>,
    override var isVisible: Boolean = false,
    override val faceImageName: String,
    override val backImageName: String,
    override val soundName: String
) : Card, CustomCardTable.LetterCardUI, DomainEntity