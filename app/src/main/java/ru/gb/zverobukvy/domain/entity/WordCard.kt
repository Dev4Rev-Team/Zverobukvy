package ru.gb.zverobukvy.domain.entity

data class WordCard(
    val word: String,
    override val typesCards: List<TypeCards>,
    val positionsGuessedLetters: MutableList<Int>,
    val url: String
) : Card
