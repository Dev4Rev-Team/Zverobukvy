package ru.gb.zverobukvy.utility.ui.image_avatar_loader

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import ru.gb.zverobukvy.utility.ui.AvatarEnum

object ExtractAvatarDrawableHelper {
    fun extractDrawable(context: Context, imageName: String): Drawable? =
            AppCompatResources.getDrawable(context, getResId(imageName))

    private fun getResId(imageName: String): Int {
        AvatarEnum.values().forEach {
            if (it.imageName == imageName)
                return it.resId
        }
        return AvatarEnum.AVATAR_CAT.resId
    }
}






