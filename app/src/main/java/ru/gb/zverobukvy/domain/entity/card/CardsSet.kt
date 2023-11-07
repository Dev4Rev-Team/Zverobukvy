package ru.gb.zverobukvy.domain.entity.card

import ru.gb.zverobukvy.domain.entity.DomainEntity

data class CardsSet(
    val typeCards: TypeCards,
    val letters: List<Char>,
    val words: List<String>
): DomainEntity