package ru.gb.zverobukvy.di.modules

import dagger.Binds
import dagger.Module
import ru.gb.zverobukvy.presentation.awards_screen.AwardsScreenViewModel
import ru.gb.zverobukvy.presentation.awards_screen.AwardsScreenViewModelImpl

@Module
interface AwardsScreenModule {


    @Binds
    fun bindAwardsScreenViewModel(viewModel: AwardsScreenViewModelImpl): AwardsScreenViewModel
}