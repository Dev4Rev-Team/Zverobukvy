package ru.gb.zverobukvy.data.mapper.mapper_impl

import ru.gb.zverobukvy.data.mapper.EntityMapper
import ru.gb.zverobukvy.data.mapper.extract_helpers.ExtractTypesCardsHelper
import ru.gb.zverobukvy.data.preferences.TypeCardsInSharedPreferences
import ru.gb.zverobukvy.domain.entity.TypeCards

class SharedPreferencesTypeCardsMapper : EntityMapper<TypeCards, TypeCardsInSharedPreferences> {
    override fun mapToData(entity: TypeCards): TypeCardsInSharedPreferences =
            TypeCardsInSharedPreferences(ExtractTypesCardsHelper.extractColor(entity))

    override fun mapToDomain(entity: TypeCardsInSharedPreferences): TypeCards =
            ExtractTypesCardsHelper.extractTypeCards(entity.nameTypeCard)
}