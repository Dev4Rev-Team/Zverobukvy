package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.resources_provider.AssetsImageCashImpl
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
interface AssetImageCashModule {

    @Singleton
    @Binds
    fun bindAssetsImageCash(assetsImageCash: AssetsImageCashImpl): AssetsImageCash
}