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

class AssertsImageCashImpl(
    context: Context,
    animalLettersCardsRepository: AnimalLettersGameRepository,
) : AssetsImageCash {
    private val mapImages: MutableMap<String, Drawable> = mutableMapOf()
    private val myCoroutineScope = CoroutineScope(Dispatchers.Default)
    private val job: Job

    init {
        job = myCoroutineScope.launch {
            animalLettersCardsRepository.getLetterCards().forEach {
                addToMap(context, it.faceImageName)
                addToMap(context, it.backImageName)
            }

            animalLettersCardsRepository.getWordCards().forEach {
                addToMap(context, it.faceImageName)
            }

            addToMap(context, "FACE.webp")

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
        url: String,
    ) {
        if (!mapImages.contains(url)) {
            mapImages[url] = getImageFromAssert(context, url)
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
}