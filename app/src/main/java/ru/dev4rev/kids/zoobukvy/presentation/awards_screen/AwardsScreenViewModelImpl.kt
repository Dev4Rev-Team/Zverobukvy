package ru.dev4rev.kids.zoobukvy.presentation.awards_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRatingProviderFactory
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.repository.ChangeRatingRepository
import javax.inject.Inject

class AwardsScreenViewModelImpl @Inject constructor(
    changeRatingRepository: ChangeRatingRepository,
    private val viewRatingProviderFactory: ViewRatingProviderFactory,
) : AwardsScreenViewModel, ViewModel() {

    private val mainAwardsLiveData = MutableLiveData<AwardsScreenState.Main>()
    private val secondAwardsLiveData = MutableLiveData<AwardsScreenState.Second>()

    private var isPossibleToClick: Boolean = false

    private var listOfAwardedPlayers: List<PlayerInVM> = listOf()

    private var playerIndex: Int = PLAYER_INIT_INDEX
    private var awardIndex: Int = AWARDS_INIT_INDEX

    init {
        //TODO Dispatchers
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val playerBeforeGame = changeRatingRepository.getPlayersBeforeGame()
                val playerAfterGame = changeRatingRepository.getPlayersAfterGame()

                listOfAwardedPlayers = convert(playerBeforeGame, playerAfterGame).orEmpty()
            }

            if (listOfAwardedPlayers.isEmpty()) mainAwardsLiveData.value =
                AwardsScreenState.Main.CancelScreen
            else {
                //onNextClickInVM()
                mainAwardsLiveData.value = AwardsScreenState.Main.StartScreen
                isPossibleToClick = false

                viewModelScope.launch {
                    delay(1000L)
                    onNextClickInVM()
                }
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

            if (newRatingProvider.getRank() != oldRatingProvider.getRank()) {
                (awardsList).add(
                    AwardsScreenState.Second.RankIncreaseState(
                        oldRatingProvider.getRank(), newRatingProvider.getRank()
                    )
                )
            }

            val changeOrangeViewRating: Int =
                newRatingProvider.getOrangeRating().rating - oldRatingProvider.getOrangeRating().rating
            val changeGreenViewRating: Int =
                newRatingProvider.getGreenRating().rating - oldRatingProvider.getGreenRating().rating
            val changeBlueViewRating: Int =
                newRatingProvider.getBlueRating().rating - oldRatingProvider.getBlueRating().rating
            val changeVioletViewRating: Int =
                newRatingProvider.getVioletRating().rating - oldRatingProvider.getVioletRating().rating


            if (awardsList.isNotEmpty()) {
                PlayerInVM(
                    AwardsScreenState.Main.AwardedPlayerState(
                        playerStates.first.name,
                        playerStates.first.avatar,
                        oldRatingProvider.getRank(),
                        oldRatingProvider.getOrangeRating(), changeOrangeViewRating,
                        oldRatingProvider.getGreenRating(), changeGreenViewRating,
                        oldRatingProvider.getBlueRating(), changeBlueViewRating,
                        oldRatingProvider.getVioletRating(), changeVioletViewRating
                    ), awardsList
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
        if (isPossibleToClick) {
            isPossibleToClick = false
            onNextClickInVM()
        }
    }

    // TODO Apply awards into PlayerInVM
    private fun onNextClickInVM() {
        viewModelScope.launch  {
            if (awardIndex < listOfAwardedPlayers[playerIndex].awardsList.size - 1) {
                awardIndex++
                if (playerIndex == 0) {
                    mainAwardsLiveData.value = listOfAwardedPlayers[playerIndex].player
                    delay(2500L)
                }
                secondAwardsLiveData.value =
                    listOfAwardedPlayers[playerIndex].awardsList[awardIndex]
                autoNextClickWithDelay()
            } else if (playerIndex < listOfAwardedPlayers.size - 1) {
                playerIndex++
                awardIndex = 0
                mainAwardsLiveData.value = listOfAwardedPlayers[playerIndex].player
                delay(1900L)
                secondAwardsLiveData.value =
                    listOfAwardedPlayers[playerIndex].awardsList[awardIndex]
                autoNextClickWithDelay()
            } else {
                delay(1000L)
                mainAwardsLiveData.value = AwardsScreenState.Main.CancelScreen
            }
            delay(300L)
            isPossibleToClick = true
        }
    }

    private suspend fun autoNextClickWithDelay() {
        /*delay(4000L)
        onNextClickInVM()*/
    }

    data class PlayerInVM(
        val player: AwardsScreenState.Main.AwardedPlayerState,
        val awardsList: List<AwardsScreenState.Second>,
    )

    companion object {

        const val PLAYER_INIT_INDEX = 0
        const val AWARDS_INIT_INDEX = -1
    }
}