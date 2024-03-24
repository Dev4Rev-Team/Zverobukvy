package ru.dev4rev.kids.zoobukvy.domain.entity.card

import ru.dev4rev.kids.zoobukvy.domain.entity.DomainEntity

enum class TypeCards (val id: Int): DomainEntity {
    ORANGE(1), GREEN(2), BLUE(4), VIOLET(8)
}