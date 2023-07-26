package ru.gb.zverobukvy.domain.entity

data class WordCard(
    val word: String,
    override val typesCards: List<TypeCards>,
    val positionsGuessedLetters: MutableList<Int> = mutableListOf(),
    val url: String
) : Card
