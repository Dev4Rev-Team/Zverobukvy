package ru.gb.zverobukvy.di

import dagger.BindsInstance
import dagger.Subcomponent
import ru.gb.zverobukvy.di.modules.AnimalLettersGameModule
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyViewModelImpl

@AnimalLettersGameScope
@Subcomponent(modules = [AnimalLettersGameModule::class])
interface AnimalLettersGameSubcomponent {

    val viewModel: GameZverobukvyViewModelImpl

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance typesCards: List<TypeCards>,
            @BindsInstance players: List<PlayerInGame>,
        ): AnimalLettersGameSubcomponent
    }
}