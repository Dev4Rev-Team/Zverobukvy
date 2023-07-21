package ru.gb.zverobukvy.interactor

import ru.gb.zverobukvy.model.app_state.AnimalLettersState
import ru.gb.zverobukvy.model.dto.TypeCards
import ru.gb.zverobukvy.model.repository.IAnimalLettersCardsRepository

class AnimalLettersInteractor(
    private val animalLettersCardsRepository: IAnimalLettersCardsRepository
): IAnimalLettersInteractor {

    override fun getStartGameState(
        typesCards: List<TypeCards>,
        players: List<String>
    ): List<AnimalLettersState> {
        TODO("Not yet implemented")
    }

    override fun getCurrentGameState(selectedPosition: Int): List<AnimalLettersState> {
        TODO("Not yet implemented")
    }

}