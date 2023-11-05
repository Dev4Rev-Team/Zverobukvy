package ru.gb.zverobukvy.data.resources_provider

import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.gb.zverobukvy.domain.repository.AnimalLettersGameRepository
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class AssetsImageCashImpl @Inject constructor(
    context: Context,
    animalLettersCardsRepository: AnimalLettersGameRepository,
) : AssetsImageCash {
    private val mapImages: MutableMap<String, Drawable> = mutableMapOf()
    private val myCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val job: Job

    init {
        job = myCoroutineScope.launch {
            animalLettersCardsRepository.getLetterCards().forEach {
                try {
                    addToMap(
                        context,
                        it.faceImageName,
                        ASSETS_PATH_IMAGE_LETTERS + it.faceImageName
                    )
                } catch (e: Exception) {
                    throw IllegalStateException("no element image letter ${it.faceImageName}")
                }
                try {
                    addToMap(context, it.backImageName, ASSETS_PATH_IMAGE_SYSTEM + it.backImageName)
                } catch (e: Exception) {
                    throw IllegalStateException("no element image system ${it.backImageName}")
                }
            }

            animalLettersCardsRepository.getWordCards().forEach {
                try {
                    addToMap(context, it.faceImageName, ASSETS_PATH_IMAGE_WORDS + it.faceImageName)
                } catch (e: Exception) {
                    throw IllegalStateException("no element image word ${it.faceImageName}")
                }
            }

            try {
                addToMap(context, IMAGE_FACE_CARD, ASSETS_PATH_IMAGE_SYSTEM + IMAGE_FACE_CARD)
            } catch (e: Exception) {
                throw IllegalStateException("no element image system $IMAGE_FACE_CARD ")
            }
        }
    }

    override fun getImage(url: String): Drawable {
        if (!job.isCompleted) {
            runBlocking {
                job.join()
            }
        }
        return mapImages[url] ?: throw IllegalArgumentException("asset $url not found")
    }

    private fun addToMap(
        context: Context,
        name: String,
        url: String,
    ) {
        if (!mapImages.contains(name)) {
            mapImages[name] = getImageFromAssert(context, url)
        }
    }

    private fun getImageFromAssert(context: Context, src: String): Drawable {
        try {
            val ims: InputStream = context.assets.open(src)
            val d = Drawable.createFromStream(ims, null)
            ims.close()
            return d ?: throw IllegalArgumentException("asset $src not found")
        } catch (ex: IOException) {
            throw IllegalArgumentException("asset $src not found")
        }
    }

    companion object {
        const val ASSETS_PATH_IMAGE_LETTERS = "images/letters/"
        const val ASSETS_PATH_IMAGE_WORDS = "images/words/"
        const val ASSETS_PATH_IMAGE_SYSTEM = "images/system/"

        const val IMAGE_FACE_CARD = "FACE.webp"
    }
}