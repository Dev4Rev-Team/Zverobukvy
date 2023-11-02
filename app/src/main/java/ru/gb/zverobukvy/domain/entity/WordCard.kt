package ru.gb.zverobukvy.domain.entity

import ru.gb.zverobukvy.presentation.customview.CustomWordView

data class WordCard(
    override val word: String,
    val typesCards: MutableSet<TypeCards> = mutableSetOf(),
    override val positionsGuessedLetters: MutableList<Int> = mutableListOf(),
    val faceImageName: String,
    override val soundName: String
) : Card, CustomWordView.WordCardUI, DomainEntity
