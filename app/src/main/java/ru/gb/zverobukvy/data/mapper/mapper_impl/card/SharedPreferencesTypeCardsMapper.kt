package ru.gb.zverobukvy.data.mapper.mapper_impl.card

import ru.gb.zverobukvy.data.mapper.EntityMapper
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.gb.zverobukvy.data.preferences.TypeCardsInSharedPreferences
import ru.gb.zverobukvy.domain.entity.card.TypeCards

class SharedPreferencesTypeCardsMapper : EntityMapper<TypeCards, TypeCardsInSharedPreferences> {
    override fun mapToData(entity: TypeCards): TypeCardsInSharedPreferences =
            TypeCardsInSharedPreferences(ExtractTypesCardsHelper.extractColor(entity))

    override fun mapToDomain(entity: TypeCardsInSharedPreferences): TypeCards =
            ExtractTypesCardsHelper.extractTypeCards(entity.nameTypeCard)
}