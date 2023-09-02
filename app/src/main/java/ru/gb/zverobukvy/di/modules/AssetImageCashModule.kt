package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.resources_provider.AssertsImageCashImpl
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash

@Module(includes = [RepositoryModule::class])
interface AssetImageCashModule {

    @Binds
    fun bindAssetsImageCash(assertImageCash: AssertsImageCashImpl): AssetsImageCash
}