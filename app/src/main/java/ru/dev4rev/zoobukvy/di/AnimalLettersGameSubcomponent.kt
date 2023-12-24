package ru.dev4rev.zoobukvy.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.dev4rev.zoobukvy.di.modules.AnimalLettersGameModule
import ru.dev4rev.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.zoobukvy.presentation.animal_letters_game.AnimalLettersGameViewModelImpl
import ru.dev4rev.zoobukvy.presentation.animal_letters_game.game_is_over_dialog.GameIsOverDialogViewModelImpl
import ru.dev4rev.zoobukvy.presentation.customview.AssetsImageCash
import ru.dev4rev.zoobukvy.presentation.sound.SoundEffectPlayer

@AnimalLettersGameScope
@Subcomponent(modules = [AnimalLettersGameModule::class])
interface AnimalLettersGameSubcomponent {

    val viewModel: AnimalLettersGameViewModelImpl

    val gameIsOverDialogViewModel: GameIsOverDialogViewModelImpl

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