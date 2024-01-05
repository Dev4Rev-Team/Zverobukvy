package ru.dev4rev.kids.zoobukvy.domain.entity.card

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomWordView

data class WordCard(
    override val word: String,
    val typesCards: MutableSet<TypeCards> = mutableSetOf(),
    override val positionsGuessedLetters: MutableList<Int> = mutableListOf(),
    val faceImageName: String,
    override val soundName: String
) : Card, CustomWordView.WordCardUI, DomainEntity