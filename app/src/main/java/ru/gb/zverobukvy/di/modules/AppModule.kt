package ru.gb.zverobukvy.di.modules

import dagger.Module
import ru.gb.zverobukvy.di.AnimalLettersGameSubcomponent

@Module(
    subcomponents = [AnimalLettersGameSubcomponent::class],
    includes = [MainMenuModule::class, AssetImageCashModule::class]
)
interface AppModule