package ru.gb.zverobukvy.presentation.awards_screen

import ru.gb.zverobukvy.data.view_rating_provider.Rank
import ru.gb.zverobukvy.data.view_rating_provider.ViewRating
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.player.Avatar
import ru.gb.zverobukvy.presentation.awards_screen.AwardsScreenState.Second.RankIncreaseState
import ru.gb.zverobukvy.presentation.awards_screen.AwardsScreenState.Second.ViewRatingIncreaseState

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
        ) : AwardsScreenState.Main

        /** Состояние приходящее после отображения всех наград или их полного отсутствия
         */
        object CancelScreen : AwardsScreenState.Main
    }


    sealed interface Second : AwardsScreenState {

        /** Состояние повышения ранга игрока
         */
        data class RankIncreaseState(
            val typeCards: TypeCards,
            val oldRank: Rank,
            val newRank: Rank,
        ) : AwardsScreenState.Second

        /** Состояние повышения визуального рейтинга игрока
         */
        data class ViewRatingIncreaseState(
            val oldViewRating: ViewRating,
            val newViewRating: ViewRating,
        ) : AwardsScreenState.Second

    }
}