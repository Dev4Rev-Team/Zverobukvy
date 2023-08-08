package ru.gb.zverobukvy.domain.entity

import ru.gb.zverobukvy.presentation.customview.CustomWordView

data class WordCard(
    override val word: String,
    override val typesCards: List<TypeCards>,
    override val positionsGuessedLetters: MutableList<Int> = mutableListOf(),
    val faceImageName: String
) : Card, CustomWordView.WordCardUI
