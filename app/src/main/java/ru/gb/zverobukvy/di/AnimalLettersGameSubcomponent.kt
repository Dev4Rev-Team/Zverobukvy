package ru.gb.zverobukvy.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.gb.zverobukvy.di.modules.AnimalLettersGameModule
import ru.gb.zverobukvy.domain.entity.player.PlayerInGame
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer

@AnimalLettersGameScope
@Subcomponent(modules = [AnimalLettersGameModule::class])
interface AnimalLettersGameSubcomponent {

    val viewModel: AnimalLettersGameViewModelImpl

    val assetsImageCash: AssetsImageCash

    val soundEffectPlayer: SoundEffectPlayer

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance typesCards: List<TypeCards>,
            @BindsInstance players: List<PlayerInGame>,
        ): AnimalLettersGameSubcomponent
    }
}