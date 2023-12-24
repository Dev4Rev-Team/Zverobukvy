package ru.dev4rev.zoobukvy.data.mapper.mapper_impl.card

import ru.dev4rev.zoobukvy.data.mapper.EntityMapperToDomain
import ru.dev4rev.zoobukvy.data.room.entity.card.WordCardInDatabase
import ru.dev4rev.zoobukvy.domain.entity.card.WordCard

class WordCardMapperToDomain: EntityMapperToDomain<WordCard, WordCardInDatabase> {
    override fun mapToDomain(entity: WordCardInDatabase): WordCard =
        entity.let{
            WordCard(
                word = it.word,
                faceImageName = it.faceImageName,
                soundName = it.soundName
            )
        }
}