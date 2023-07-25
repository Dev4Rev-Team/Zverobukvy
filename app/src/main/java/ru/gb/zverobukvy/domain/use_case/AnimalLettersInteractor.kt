package ru.gb.zverobukvy.domain.use_case

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.gb.zverobukvy.data.repository_impl.IAnimalLettersCardsRepository
import ru.gb.zverobukvy.domain.entity.GameState
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
    private var gameState: GameState? = null
    private var gamingWords: Queue<WordCard>? = null
    private val gameStateFlow: MutableStateFlow<GameState?> = MutableStateFlow(gameState)

    override fun subscribeToGameState(): StateFlow<GameState?> {
        TODO("Not yet implemented")
    }

    override fun startGame() {
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