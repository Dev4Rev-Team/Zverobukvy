package ru.gb.zverobukvy.domain.repository.animal_letter_game

import ru.gb.zverobukvy.domain.repository.PlayersRepository

interface AnimalLettersGameRepository : LetterCardsRepository, WordCardsRepository,
    CardsSetRepository, PlayersRepository {
}