package ru.dev4rev.kids.zoobukvy.data.mapper.mapper_impl.card

import ru.dev4rev.kids.zoobukvy.data.mapper.EntityMapper
import ru.dev4rev.kids.zoobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.dev4rev.kids.zoobukvy.data.preferences.TypeCardsInSharedPreferences
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards

class SharedPreferencesTypeCardsMapper : EntityMapper<TypeCards, TypeCardsInSharedPreferences> {
    override fun mapToData(entity: TypeCards): TypeCardsInSharedPreferences =
            TypeCardsInSharedPreferences(ExtractTypesCardsHelper.extractColor(entity))

    override fun mapToDomain(entity: TypeCardsInSharedPreferences): TypeCards =
            ExtractTypesCardsHelper.extractTypeCards(entity.nameTypeCard)
}