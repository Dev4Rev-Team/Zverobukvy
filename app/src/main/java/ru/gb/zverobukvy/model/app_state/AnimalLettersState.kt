package ru.gb.zverobukvy.model.app_state

sealed class AnimalLettersState {
    //передаем стартовое поле (один раз в начале игры)
    class StartLettersField(val lettersField: List<Char>) : AnimalLettersState()
    //передаем слово, которое ищем (в начале игры и после того, как предыдущее слово отгадали)
    class ChangingGamingWord(val gamingWord: String) : AnimalLettersState()
    //передаем счет: список имен игроков+количество угаданных ими карточек (в начале игры 0:0,
    // затем после каждого отгаданного слова)
    class ChangingScore(val score: List<Pair<String, Int>>) : AnimalLettersState()
    //передаем имя игрока, чей должен быть ход
    class ChangingMove(val player: String) : AnimalLettersState()
    //передаем список буквенных карточек, которые надо перевернуть: при клике на карточку
    // (перевернуть одну карточку), если буква неправильная (перевернуть одну карточку),
    // когда слово отгадали(все отгаданные буквы перевернуть)
    class FlipLettersCards(val flippedLettersCards: List<Int>): AnimalLettersState()
    // передаем позицию правильной буквы в составе слова, чтобы подсветить ее другим цветом в слове
    class RightLetter(val positionLetterInWord: Int): AnimalLettersState()
    // передаем информацию о том, что выбрана не правильная буква (необходимо придумать, как проинформировать
    // игроков, что буква не правильная, например кратковременно подсветить слово красным цветом)
    object HighlightWrongLetter: AnimalLettersState()
}
