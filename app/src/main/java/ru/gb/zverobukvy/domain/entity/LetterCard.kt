package ru.gb.zverobukvy.domain.entity

import ru.gb.zverobukvy.presentation.customview.CustomCardTable

data class LetterCard(
    override val letter: Char,
    override val typesCards: List<TypeCards>,
    override var isVisible: Boolean = false,
    override val faceImageName: String,
    override val backImageName: String = BACK_IMAGE_NAME
) : Card, CustomCardTable.LetterCardUI {
    companion object {
        const val BACK_IMAGE_NAME = "BACK_IMAGE.jpg"
    }
}

