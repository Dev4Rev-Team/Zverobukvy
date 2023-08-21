package ru.gb.zverobukvy

import android.content.Context
import android.graphics.drawable.Drawable
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException

class FakeAssetsImageCash(context: Context) : AssetsImageCash {
    private val contextApp: Context = context.applicationContext
    override fun getImage(url: String): Drawable {
        return when (url) {
            "img2.png" -> getImageFromAssert("img2.png")
            "img3.png" -> getImageFromAssert("img3.png")
            "img4.png" -> getImageFromAssert("img4.png")
            "img5.png" -> getImageFromAssert("img5.png")
            "img6.png" -> getImageFromAssert("img6.png")
            "img7.png" -> getImageFromAssert("img7.png")
            "img8.png" -> getImageFromAssert("img8.png")
            "img9.png" -> getImageFromAssert("img9.png")
            else -> getImageFromAssert("img7.png")
        }
    }

    private fun getImageFromAssert(src: String): Drawable {
        try {
            val ims: InputStream = contextApp.assets.open(src)
            val d = Drawable.createFromStream(ims, null)
            ims.close()
            return d ?: throw IllegalArgumentException("Err assets $src")
        } catch (ex: IOException) {
            throw IllegalArgumentException("Err assets $src")
        }
    }

}