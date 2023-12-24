package ru.dev4rev.zoobukvy.data.view_rating_provider

import ru.dev4rev.zoobukvy.R

enum class Decoration(val idColor: Int) {
    DEFAULT(R.color.transparent), BRONZE(R.color.color_bronze), SILVER(R.color.color_silver), GOLD(R.color.color_gold), DIAMOND(
        R.color.border_rank_legend
    )
}