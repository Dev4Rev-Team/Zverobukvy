package ru.gb.zverobukvy.data.image_avatar_loader

import android.widget.ImageView
import ru.gb.zverobukvy.domain.entity.Avatar

interface ImageAvatarLoader {
    fun loadImageAvatar(avatar: Avatar, container: ImageView)
}