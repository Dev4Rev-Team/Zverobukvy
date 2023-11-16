package ru.gb.zverobukvy.data.view_rating_provider

import ru.gb.zverobukvy.configuration.Conf.Companion.DECORATION_RATING
import ru.gb.zverobukvy.configuration.Conf.Companion.EXPERT_RATING
import ru.gb.zverobukvy.configuration.Conf.Companion.GENIUS_RATING
import ru.gb.zverobukvy.configuration.Conf.Companion.HERO_RATING
import ru.gb.zverobukvy.configuration.Conf.Companion.LEGEND_RATING
import ru.gb.zverobukvy.configuration.Conf.Companion.MASTER_RATING
import ru.gb.zverobukvy.domain.entity.player.Rating

class ViewRatingProviderImpl(private val rating: Rating) : ViewRatingProvider {
    override fun getRank(): Rank {
        with(rating) {
            val orangeAndHarderRating = orangeRating + greenRating + blueRating + violetRating
            val greenAndHarderRating = greenRating + blueRating + violetRating
            val blueAndHarderRating = blueRating + violetRating
            return if (LEGEND_RATING.let { orangeRating > it && greenRating > it && blueRating > it && violetRating > it })
                Rank.LEGEND
            else if (violetRating > HERO_RATING)
                Rank.HERO
            else if (blueAndHarderRating > GENIUS_RATING)
                Rank.GENIUS
            else if (greenAndHarderRating > MASTER_RATING)
                Rank.MASTER
            else if (orangeAndHarderRating > EXPERT_RATING)
                Rank.EXPERT
            else
                Rank.LEARNER
        }
    }

    override fun getOrangeRating(): ViewRating = getViewRating(rating.orangeRating)

    override fun getGreenRating(): ViewRating = getViewRating(rating.greenRating)

    override fun getBlueRating(): ViewRating = getViewRating(rating.blueRating)

    override fun getVioletRating(): ViewRating = getViewRating(rating.violetRating)

    private fun getViewRating(rating: Int): ViewRating =
        DECORATION_RATING.let {
            if (rating < it)
                ViewRating(Decoration.DEFAULT, rating)
            else if (rating < 2 * it)
                ViewRating(Decoration.BRONZE, rating - it)
            else if (rating < 3 * it)
                ViewRating(Decoration.SILVER, rating - 2 * it)
            else if (rating < 4 * it)
                ViewRating(Decoration.GOLD, rating - 3 * it)
            else
                ViewRating(Decoration.DIAMOND, rating)
        }
}