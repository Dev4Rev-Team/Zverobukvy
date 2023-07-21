package ru.gb.zverobukvy.interactor

import ru.gb.zverobukvy.model.app_state.AnimalLettersState
import ru.gb.zverobukvy.model.dto.TypeCards

interface IAnimalLettersInteractor {
    fun getStartGameState(typesCards: List<TypeCards>, players: List<String>): List<AnimalLettersState>

    fun getCurrentGameState(selectedPosition: Int): List <AnimalLettersState>
}