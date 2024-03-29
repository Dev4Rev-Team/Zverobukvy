package ru.dev4rev.kids.zoobukvy.domain.entity.game_state

import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard

data class GameField(
    val lettersField: List<LetterCard>,
    val gamingWordCard: WordCard?
)