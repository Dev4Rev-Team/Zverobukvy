package ru.dev4rev.kids.zoobukvy.presentation.awards_screen

import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.Rank
import ru.dev4rev.kids.zoobukvy.data.view_rating_provider.ViewRating
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Avatar
import ru.dev4rev.kids.zoobukvy.presentation.awards_screen.AwardsScreenState.Second.RankIncreaseState
import ru.dev4rev.kids.zoobukvy.presentation.awards_screen.AwardsScreenState.Second.ViewRatingIncreaseState

sealed interface AwardsScreenState {

    sealed interface Main : AwardsScreenState {

        /** Состояние награждения нового игрока. Сразу за ним следуют
         * либо [RankIncreaseState], либо [ViewRatingIncreaseState]
         *
         * Приход данного состояния сопровождается очисткой экрана от предыдущих наград
         */
        data class AwardedPlayerState(
            val playerName: String,
            val playerAvatar: Avatar,
            val rank: Rank,
            var orangeViewRating: ViewRating,
            val changeOrangeViewRating: Int,
            var greenViewRating: ViewRating,
            val changeGreenViewRating: Int,
            var blueViewRating: ViewRating,
            val changeBlueViewRating: Int,
            var violetViewRating: ViewRating,
            val changeVioletViewRating: Int,
        ) : Main

        /** Состояние приходящее после отображения всех наград или их полного отсутствия
         */
        object CancelScreen : Main

        /** Состояние приходящее при инициализации ViewModel
         */
        object StartScreen : Main
    }


    sealed interface Second : AwardsScreenState {

        /** Состояние повышения ранга игрока
         */
        data class RankIncreaseState(
            val oldRank: Rank,
            val newRank: Rank,
        ) : Second

        /** Состояние повышения визуального рейтинга игрока
         */
        data class ViewRatingIncreaseState(
            val typeCards: TypeCards,
            val oldViewRating: ViewRating,
            val newViewRating: ViewRating,
        ) : Second

    }
}