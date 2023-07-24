package ru.gb.zverobukvy.domain.entity

data class WordCard(val word: String, override val typesCards: List<TypeCards>) : Card
