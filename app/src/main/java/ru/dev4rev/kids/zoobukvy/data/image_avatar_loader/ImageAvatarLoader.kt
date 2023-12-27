package ru.dev4rev.kids.zoobukvy.data.image_avatar_loader

import android.widget.ImageView
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar

interface ImageAvatarLoader {
    fun loadImageAvatar(avatar: Avatar, container: ImageView)
}