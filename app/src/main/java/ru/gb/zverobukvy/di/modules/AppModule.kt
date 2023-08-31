package ru.gb.zverobukvy.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.gb.zverobukvy.di.AnimalLettersGameSubcomponent
import ru.gb.zverobukvy.di.MainMenuScope
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment

@Module(
    subcomponents = [AnimalLettersGameSubcomponent::class],
    includes = [
        AndroidSupportInjectionModule::class,
        MainMenuModule::class,
        ResourcesProviderModule::class,
        AssetImageCashModule::class,
        SoundEffectModule::class]
)
interface AppModule {

    @MainMenuScope
    @ContributesAndroidInjector(modules = [MainMenuModule::class])
    fun mainMenuFragmentInjector(): MainMenuFragment
}