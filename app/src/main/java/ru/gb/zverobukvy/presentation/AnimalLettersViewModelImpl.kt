package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.domain.app_state.AnimalLettersChangingState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersEntireState
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractor

class AnimalLettersViewModelImpl(private val animalLettersInteractor: AnimalLettersInteractor):
    AnimalLettersViewModel {

    override fun onActiveGame() {
        TODO("Not yet implemented")
    }

    override fun getEntireGameStateLiveData(): LiveData<AnimalLettersEntireState> {
        TODO("Not yet implemented")
    }

    override fun getChangingGameStateLiveData(): SingleEventLiveData<AnimalLettersChangingState> {
        TODO("Not yet implemented")
    }

    override fun onClickLetterCard(positionSelectedLetterCard: Int) {
        TODO("Not yet implemented")
    }

    override fun onClickNextWalkingPlayer() {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
    }

    override fun onEndGameByUser() {
        TODO("Not yet implemented")
    }

}