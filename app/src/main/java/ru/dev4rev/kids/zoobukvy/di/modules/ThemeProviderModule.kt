package ru.dev4rev.kids.zoobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.dev4rev.kids.zoobukvy.data.theme_provider.ThemeProvider
import ru.dev4rev.kids.zoobukvy.data.theme_provider.ThemeProviderImpl
import javax.inject.Singleton

@Module
interface ThemeProviderModule {

    @Singleton
    @Binds
    fun bindThemeProvider(themeProvider: ThemeProviderImpl): ThemeProvider
}