package ru.gb.zverobukvy.data.view_rating_provider

import ru.gb.zverobukvy.domain.entity.Rating

class ViewRatingProviderImpl(private val rating: Rating) : ViewRatingProvider {
    override fun getRank(): Rank {
        with(rating) {
            val orangeAndHarderRating = orangeRating + greenRating + blueRating + violetRating
            val greenAndHarderRating = greenRating + blueRating + violetRating
            val blueAndHarderRating = blueRating + violetRating
            return if (violetRating > 150)
                Rank.LEGEND
            else if (violetRating > 125)
                Rank.HERO
            else if (blueAndHarderRating > 100)
                Rank.GENIUS
            else if (greenAndHarderRating > 75)
                Rank.MASTER
            else if (orangeAndHarderRating > 50)
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
        if (rating < 100)
            ViewRating(Decoration.DEFAULT, rating)
        else if (rating < 200)
            ViewRating(Decoration.BRONZE, rating-100)
        else if (rating < 300)
            ViewRating(Decoration.SILVER, rating-200)
        else
            ViewRating(Decoration.GOLD, rating-300)

}