package ru.gb.zverobukvy.data.view_rating_provider

interface ViewRatingProvider {
    fun getRank(): Rank

    fun getOrangeRating(): ViewRating

    fun getGreenRating(): ViewRating

    fun getBlueRating(): ViewRating

    fun getVioletRating(): ViewRating
}