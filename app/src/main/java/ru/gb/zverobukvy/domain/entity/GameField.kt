package ru.gb.zverobukvy.domain.entity

data class GameField(
    val lettersField: List<LetterCard>,
    var gamingWordCard: WordCard?
)