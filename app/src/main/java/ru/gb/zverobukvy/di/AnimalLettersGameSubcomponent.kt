package ru.gb.zverobukvy.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.gb.zverobukvy.data.resources_provider.AssertsImageCashImpl
import ru.gb.zverobukvy.di.modules.AnimalLettersGameModule
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayerImpl

@AnimalLettersGameScope
@Subcomponent(modules = [AnimalLettersGameModule::class])
interface AnimalLettersGameSubcomponent {

    val viewModel: AnimalLettersGameViewModelImpl

    val assetsImageCash: AssertsImageCashImpl

    val soundEffectPlayer: SoundEffectPlayerImpl

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance typesCards: List<TypeCards>,
            @BindsInstance players: List<PlayerInGame>,
        ): AnimalLettersGameSubcomponent
    }
}