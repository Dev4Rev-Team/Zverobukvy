package ru.gb.zverobukvy.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.gb.zverobukvy.di.AnimalLettersGameScope
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameFragment

@Module
interface MainMenuModule {

    @AnimalLettersGameScope
    @ContributesAndroidInjector(modules = [AnimalLettersGameModule::class])
    fun animalLettersGameFragmentInjector(): AnimalLettersGameFragment
}