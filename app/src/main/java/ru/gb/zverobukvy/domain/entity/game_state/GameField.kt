package ru.gb.zverobukvy.domain.entity.game_state

import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.domain.entity.card.WordCard

data class GameField(
    val lettersField: List<LetterCard>,
    var gamingWordCard: WordCard?
)