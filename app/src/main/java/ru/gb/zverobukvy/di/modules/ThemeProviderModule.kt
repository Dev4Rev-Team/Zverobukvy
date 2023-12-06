package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.data.theme_provider.ThemeProvider
import ru.gb.zverobukvy.data.theme_provider.ThemeProviderImpl
import javax.inject.Singleton

@Module
interface ThemeProviderModule {

    @Singleton
    @Binds
    fun bindThemeProvider(themeProvider: ThemeProviderImpl): ThemeProvider
}