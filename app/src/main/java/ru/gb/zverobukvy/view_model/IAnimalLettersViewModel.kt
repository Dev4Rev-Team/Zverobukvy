package ru.gb.zverobukvy.view_model

import ru.gb.zverobukvy.model.dto.TypeCards

interface IAnimalLettersViewModel {
    fun onStartGame(typesCards: List<TypeCards>, players: List<String>)

    fun onClickLetterCard(selectedPosition: Int)

}