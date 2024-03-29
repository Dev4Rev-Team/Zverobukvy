package ru.dev4rev.kids.zoobukvy.domain.repository.animal_letter_game

import ru.dev4rev.kids.zoobukvy.domain.repository.PlayersRepository

interface AnimalLettersGameRepository : LetterCardsRepository, WordCardsRepository,
    CardsSetRepository, PlayersRepository {
}