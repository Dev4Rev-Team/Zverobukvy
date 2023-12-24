package ru.dev4rev.kids.zoobukvy.data.view_rating_provider

interface ViewRatingProvider {
    fun getRank(): Rank

    fun getOrangeRating(): ViewRating

    fun getGreenRating(): ViewRating

    fun getBlueRating(): ViewRating

    fun getVioletRating(): ViewRating
}