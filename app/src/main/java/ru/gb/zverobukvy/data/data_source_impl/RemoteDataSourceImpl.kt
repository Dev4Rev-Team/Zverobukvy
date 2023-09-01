package ru.gb.zverobukvy.data.data_source_impl

import ru.gb.zverobukvy.data.data_source.RemoteDataSource
import ru.gb.zverobukvy.data.retrofit.AvatarImageEntity
import ru.gb.zverobukvy.data.retrofit.RandomAvatarService
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val retrofitApi: RandomAvatarService,
) : RemoteDataSource {

    private fun getRandomSeed(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override suspend fun getRandomAvatar(): AvatarImageEntity {
        val randomSeed = getRandomSeed(DEFAULT_SEED_LENGTH)
        return retrofitApi.getSvgImageEntityBySeed(randomSeed)
    }

    override suspend fun getRandomAvatars(quantity: Int): List<AvatarImageEntity> {
        if (quantity <= 0) throw IllegalStateException(ERROR_INVALID_AVATARS_QUANTITY)

        return mutableListOf<AvatarImageEntity>().apply {
            (0..quantity).forEach { _ ->
                val randomAvatar = getRandomAvatar()
                add(randomAvatar)
            }
        }
    }

    companion object {
        const val DEFAULT_SEED_LENGTH: Int = 6

        const val ERROR_INVALID_AVATARS_QUANTITY = "Невалидное колличество"

    }
}