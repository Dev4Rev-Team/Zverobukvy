package ru.dev4rev.kids.zoobukvy.data.view_rating_provider

import ru.dev4rev.kids.zoobukvy.R

enum class Decoration(val colorId: Int) {
    DEFAULT(R.color.transparent), BRONZE(R.color.color_bronze), SILVER(R.color.color_silver), GOLD(R.color.color_gold), DIAMOND(
        R.color.border_rank_legend
    )
}