package ru.dev4rev.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.zoobukvy.data.resources_provider.AssetsImageCashImpl
import ru.dev4rev.zoobukvy.presentation.customview.AssetsImageCash
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
interface AssetImageCashModule {

    @Singleton
    @Binds
    fun bindAssetsImageCash(assetsImageCash: AssetsImageCashImpl): AssetsImageCash
}