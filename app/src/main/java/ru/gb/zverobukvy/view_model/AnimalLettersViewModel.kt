package ru.gb.zverobukvy.view_model

import ru.gb.zverobukvy.interactor.IAnimalLettersInteractor
import ru.gb.zverobukvy.model.dto.TypeCards

class AnimalLettersViewModel(private val animalLettersInteractor: IAnimalLettersInteractor):
    IAnimalLettersViewModel {
    override fun onStartGame(typesCards: List<TypeCards>, players: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onClickLetterCard(selectedPosition: Int) {
        TODO("Not yet implemented")
    }
}