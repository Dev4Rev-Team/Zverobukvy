package ru.gb.zverobukvy.data.data_source_impl

import androidx.annotation.IntRange
import ru.gb.zverobukvy.data.data_source.RemoteDataSource
import ru.gb.zverobukvy.data.retrofit.AvatarApi
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

    override suspend fun getRandomAvatar(@IntRange(0, 200) scale: Int): String {
        val randomSeed = getRandomSeed(DEFAULT_SEED_LENGTH)
        return retrofitApi.getSvgImageEntityBySeed(mapOf(
            SEED to randomSeed,
            SCALE to scale.toString()
        ))
    }

    override suspend fun getRandomAvatars(quantity: Int): List<AvatarApi> {
        if (quantity <= 0) throw IllegalStateException(ERROR_INVALID_AVATARS_QUANTITY)

        return mutableListOf<AvatarApi>().apply {
            (0..quantity).forEach { _ ->
                val randomAvatar = AvatarApi(getRandomAvatar(DEFAULT_AVATAR_SCALE))
                add(randomAvatar)
            }
        }
    }

    companion object {
        const val DEFAULT_SEED_LENGTH: Int = 6
        const val DEFAULT_AVATAR_SCALE: Int = 150

        const val SEED = "seed"
        const val SCALE = "scale"

        const val ERROR_INVALID_AVATARS_QUANTITY = "Невалидное количество"

    }
}