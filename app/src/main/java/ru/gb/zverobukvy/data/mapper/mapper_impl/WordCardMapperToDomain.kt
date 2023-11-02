package ru.gb.zverobukvy.data.mapper.mapper_impl

import ru.gb.zverobukvy.data.mapper.EntityMapperToDomain
import ru.gb.zverobukvy.data.room.entity.WordCardInDatabase
import ru.gb.zverobukvy.domain.entity.WordCard

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