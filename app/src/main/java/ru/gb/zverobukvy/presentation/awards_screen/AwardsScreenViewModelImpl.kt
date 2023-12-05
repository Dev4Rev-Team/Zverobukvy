package ru.gb.zverobukvy.presentation.awards_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.repository.ChangeRatingRepository
import javax.inject.Inject

class AwardsScreenViewModelImpl @Inject constructor(
    changeRatingRepository: ChangeRatingRepository,
    private val viewRatingProviderFactory: ViewRatingProviderFactory,
) : AwardsScreenViewModel, ViewModel() {

    private val mainAwardsLiveData = MutableLiveData<AwardsScreenState.Main>()
    private val secondAwardsLiveData = MutableLiveData<AwardsScreenState.Second>()

    private var listOfAwardedPlayers: List<PlayerInVM> = listOf()

    private var playerIndex: Int = INIT_INDEX
    private var awardIndex: Int = INIT_INDEX

    init {
        viewModelScope.launch {
            val playerBeforeGame = changeRatingRepository.getPlayersBeforeGame()
            val playerAfterGame = changeRatingRepository.getPlayersAfterGame()

            listOfAwardedPlayers = convert(playerBeforeGame, playerAfterGame).orEmpty()

            if (listOfAwardedPlayers.isEmpty())
                mainAwardsLiveData.value = AwardsScreenState.Main.CancelScreen
            else {
                delay(200L)

                mainAwardsLiveData.value = listOfAwardedPlayers[playerIndex].player
                secondAwardsLiveData.value =
                    listOfAwardedPlayers[playerIndex].awardsList[awardIndex]
            }
        }
    }

    private suspend fun convert(
        playerBeforeGame: List<Player.HumanPlayer>,
        playerAfterGame: List<Player.HumanPlayer>,
    ): List<PlayerInVM>? {

        val list = playerAfterGame.zip(playerBeforeGame).mapNotNull { playerStates ->

            val newRatingProvider = viewRatingProviderFactory.create(playerStates.first.rating)
            val oldRatingProvider = viewRatingProviderFactory.create(playerStates.second.rating)

            val awardsList = mutableListOf<AwardsScreenState.Second>()

            if (newRatingProvider.getRank() != oldRatingProvider.getRank()) {
                (awardsList).add(
                    AwardsScreenState.Second.RankIncreaseState(
                        oldRatingProvider.getRank(),
                        newRatingProvider.getRank()
                    )
                )
            }

            if (newRatingProvider.getOrangeRating().decoration != oldRatingProvider.getOrangeRating().decoration) {
                (awardsList).add(
                    AwardsScreenState.Second.ViewRatingIncreaseState(
                        TypeCards.ORANGE,
                        oldRatingProvider.getOrangeRating(),
                        newRatingProvider.getOrangeRating()
                    )
                )
            }

            if (newRatingProvider.getGreenRating().decoration != oldRatingProvider.getGreenRating().decoration) {
                (awardsList).add(
                    AwardsScreenState.Second.ViewRatingIncreaseState(
                        TypeCards.GREEN,
                        oldRatingProvider.getGreenRating(),
                        newRatingProvider.getGreenRating()
                    )
                )
            }

            if (newRatingProvider.getBlueRating().decoration != oldRatingProvider.getBlueRating().decoration) {
                (awardsList).add(
                    AwardsScreenState.Second.ViewRatingIncreaseState(
                        TypeCards.BLUE,
                        oldRatingProvider.getBlueRating(),
                        newRatingProvider.getBlueRating()
                    )
                )
            }

            if (newRatingProvider.getVioletRating().decoration != oldRatingProvider.getVioletRating().decoration) {
                (awardsList).add(
                    AwardsScreenState.Second.ViewRatingIncreaseState(
                        TypeCards.VIOLET,
                        oldRatingProvider.getVioletRating(),
                        newRatingProvider.getVioletRating()
                    )
                )
            }

            if (awardsList.isNotEmpty()) {
                PlayerInVM(
                    AwardsScreenState.Main.AwardedPlayerState(
                        playerStates.first.name,
                        playerStates.first.avatar
                    ),
                    awardsList
                )
            } else {
                null
            }
        }

        return list.ifEmpty { null }
    }

    override fun getMainAwardsLiveData(): LiveData<AwardsScreenState.Main> {
        return mainAwardsLiveData
    }

    override fun getSecondAwardsLiveData(): LiveData<AwardsScreenState.Second> {
        return secondAwardsLiveData
    }

    override fun onNextClick() {

        if (awardIndex < listOfAwardedPlayers[playerIndex].awardsList.size - 1) {
            awardIndex++
            secondAwardsLiveData.value = listOfAwardedPlayers[playerIndex].awardsList[awardIndex]
        } else if (playerIndex < listOfAwardedPlayers.size - 1) {
            playerIndex++
            awardIndex = 0
            mainAwardsLiveData.value = listOfAwardedPlayers[playerIndex].player
            secondAwardsLiveData.value = listOfAwardedPlayers[playerIndex].awardsList[awardIndex]
        } else {
            mainAwardsLiveData.value = AwardsScreenState.Main.CancelScreen
        }
    }

    data class PlayerInVM(
        val player: AwardsScreenState.Main.AwardedPlayerState,
        val awardsList: List<AwardsScreenState.Second>,
    )

    companion object {

        const val INIT_INDEX = 0
    }
}