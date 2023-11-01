package ru.gb.zverobukvy.domain.entity

data class CardsSet(
    val typeCards: TypeCards,
    val letters: List<Char>,
    val words: List<String>
): DomainEntity