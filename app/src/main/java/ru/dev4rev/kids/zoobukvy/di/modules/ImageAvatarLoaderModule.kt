package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import javax.inject.Singleton

@Module
interface ImageAvatarLoaderModule {

    @Singleton
    @Binds
    fun bindImageAvatarLoader(imageAvatarLoader: ImageAvatarLoaderImpl): ImageAvatarLoader
}