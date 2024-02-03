package ru.dev4rev.kids.zoobukvy.data.view_rating_provider

import ru.dev4rev.kids.zoobukvy.R

enum class Rank(val rankNameId: Int, val rankTextColorId: Int, val borderRankColorId: Int) {
    DEFAULT(
        R.string.username_default,
        R.color.transparent,
        R.color.transparent
    ),
    LEARNER(
        R.string.learner,
        R.color.rank_learner,
        R.color.border_rank_learner
    ),
    EXPERT(
        R.string.expert,
        R.color.rank_expert,
        R.color.border_rank_expert
    ),
    MASTER(
        R.string.master,
        R.color.rank_master,
        R.color.border_rank_master
    ),
    GENIUS(
        R.string.genius,
        R.color.rank_genius,
        R.color.border_rank_genius
    ),
    HERO(
        R.string.hero,
        R.color.rank_hero,
        R.color.border_rank_hero
    ),
    LEGEND(
        R.string.legend,
        R.color.rank_legend,
        R.color.border_rank_legend
    )
}