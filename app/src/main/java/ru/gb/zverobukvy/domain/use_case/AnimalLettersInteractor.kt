package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.data.repository_impl.IAnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.app_state.AnimalLettersChangingState
import ru.gb.zverobukvy.domain.app_state.AnimalLettersEntireState
import ru.gb.zverobukvy.domain.entity.LetterCard
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.entity.WordCard
import java.util.LinkedList
import java.util.Queue

/**
 Начальные наброски
 */
class AnimalLettersInteractor(
    private val animalLettersCardsRepository: IAnimalLettersCardsRepository,
    override val typesCards: List<TypeCards>,
    override val players: List<Player>
) : IAnimalLettersInteractor {
    private var lettersField: MutableList<LetterCard>? = null

    private var gamingWords: Queue<WordCard>? = null

    override fun subscribeToEntireGameState(): StateFlow<AnimalLettersEntireState> {
        TODO("Not yet implemented")
    }

    override fun subscribeToChangingGameState(): StateFlow<AnimalLettersChangingState> {
        TODO("Not yet implemented")
    }

    override fun activeGameState() {
        TODO("Not yet implemented")
    }

    override fun selectionLetterCard() {
        TODO("Not yet implemented")
    }

    override fun endGameByUser() {
        TODO("Not yet implemented")
    }

    private suspend fun getStartedLettersField(typesCards: List<TypeCards>): MutableList<LetterCard> =
        DealCards.getKitCards(animalLettersCardsRepository.getLetterCards(), typesCards)
            .toMutableList()

    private suspend fun getGamingWords(typesCards: List<TypeCards>): Queue<WordCard> =
        LinkedList(DealCards.getKitCards(animalLettersCardsRepository.getWordCards(), typesCards))

}