package ru.gb.zverobukvy.domain.entity

data class LetterCard(
    val letter: Char,
    override val typesCards: List<TypeCards>,
    var isVisible: Boolean = false,
    val faceImageName: String,
    val backImageName: String = BACK_IMAGE_NAME
) : Card {
    companion object {
        const val BACK_IMAGE_NAME = "back_image.png"
    }
}

