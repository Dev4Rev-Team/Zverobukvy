package ru.gb.zverobukvy.domain.entity

data class LetterCard(
    val letter: Char,
    override val typesCards: List<TypeCards>,
    var isVisible: Boolean = false,
    val url: String
) : Card
