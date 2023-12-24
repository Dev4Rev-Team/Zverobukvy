package ru.dev4rev.zoobukvy.domain.entity.card

import ru.dev4rev.zoobukvy.domain.entity.DomainEntity

data class CardsSet(
    val typeCards: TypeCards,
    val letters: List<Char>,
    val words: List<String>
): DomainEntity