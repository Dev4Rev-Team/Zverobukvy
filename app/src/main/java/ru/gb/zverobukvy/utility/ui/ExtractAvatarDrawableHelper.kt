package ru.gb.zverobukvy.utility.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import ru.gb.zverobukvy.domain.entity.Avatar

object ExtractAvatarDrawableHelper {
    fun extractDrawable(context: Context, avatar: Avatar): Drawable? {
        return if (avatar.isStandard)
            AppCompatResources.getDrawable(context, getResId(avatar.imageName))
        else

            //TODO загрузка drawable из файла
            AppCompatResources.getDrawable(context, AvatarEnum.AVATAR_CAT.resId)
    }

    private fun getResId(imageName: String): Int {
        AvatarEnum.values().forEach {
            if (it.imageName == imageName)
                return it.resId
        }
        return AvatarEnum.AVATAR_CAT.resId
    }
}






