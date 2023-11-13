package ru.gb.zverobukvy.configuration

class Conf {
    companion object{

        /**
         * Размер поля
         */
         const val SIZE_ORANGE = 9
         const val SIZE_GREEN = 12
         const val SIZE_BLUE = 16
         const val SIZE_VIOLET = 25

        /**
         * Настройки компьютера
         *
         * Описание логики
         * smart -  во сколько раз игрок играет лучше игрока нажимающего случайно по картам [1:inf]
         * sizeTable - количество букв в игре
         * val mulMove = min(MoveNumberInWord / sizeTable, SMART_MAX_MOVE)
         * val mulWord = min(GuessedWord * SMART_ADD_FOR_ONE_GUESSED_WORD, SMART_MAX_WORD)
         * smartLevel = 1 + (smart - 1) * mulMove + (smart - 1) * mulWord
         *
         * val correctCard = InvisibleCorrectLetters.size
         * lettersRemember.size() = 0:MAX_REMEMBER
         * val invisibleCard = correctCard + IncorrectLetters.size - lettersRemember.size()
         * probabilityRandom = correctCard / invisibleCard
         *
         * probability = probabilityRandom * smartLevel * SMART_COMPUTER
         *
         * if (remember){ openCorrectLetters return }
         * if (Random <= probability) { openCorrectLetters }
         * else { openIncorrectLetters if (isNotOpenIncorrectLetters) { openCorrectLetters }}
         */
        const val MAX_REMEMBER = 3
        const val SMART_COMPUTER = 1f
        const val SMART_MAX_MOVE = 2f
        const val SMART_MAX_WORD  = 2f
        const val SMART_ADD_FOR_ONE_GUESSED_WORD = 0.1f
        // Среднее количество бувк в словах
        const val AVERAGE_LETTERS_IN_WORD = 5f


        /**
         * Настройка игры
         */
        // задержка начала затемнения экрана(переход хода)
        const val START_DELAY_ANIMATION_SCREEN_DIMMING = 500L
        // время за которое происходит затемнение экрана
        const val DURATION_ANIMATION_SCREEN_DIMMING = 300L

        // задержка озвучивания слова после его появления
        const val DELAY_SOUND_WORD = 500L
        // задержка озвучивания эффекта(правильная буква/ неправильная буква/ правильное слово) после нажатия на карту
        const val DELAY_SOUND_EFFECT = 700L
        // задержка озвучивания буквы после нажатия на карту
        const val DELAY_SOUND_LETTER = 500L
        // задержка озвучивания буквы после нажатия на открытую карту
        const val DELAY_SOUND_REPEAT = 0L
        // задержка разрешения нажимать на следующую карту
        const val DELAY_ENABLE_CLICK_LETTERS_CARD = 100L
        // фон открытой карту с буквой
        const val IMAGE_CARD_FOREGROUND = "FACE.webp"

        /**
         * Задержки во viewModel
         */
        // Стандартная задержка между отправляемыми состояниями
        const val STATE_DELAY = 2000L
        // Задержка перед ходом компьютера после смены игрока
        const val COMPUTER_DELAY = 700L
        // Задержка перед повторным ходом компьютера
        const val REPEAT_COMPUTER_DELAY = 1500L
        // Задержка между состояниям InvalidLetters и автоматической сменой игрока
        // Слишком маленкое значение приводит к багам !!
        const val AUTO_NEXT_PLAYER_DELAY = 1500L

    }
}