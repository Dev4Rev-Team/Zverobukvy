package ru.gb.zverobukvy.configuration

class Conf {
    companion object {

        /**
         * Игровое поле
         */
        const val SIZE_ORANGE = 9
        const val SIZE_GREEN = 12
        const val SIZE_BLUE = 16
        const val SIZE_VIOLET = 20

        // кол-во слов животных для отгадывания в одном раунде
        const val NUMBER_OF_WORD = 10
        // максимальное кол-во букв в слове
        const val MAX_NUMBER_OF_LETTERS_IN_WORD = 7
        // проверить базу данных на корректность
        const val IS_CHECK_DATA = false

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
        const val SMART_MAX_WORD = 2f
        const val SMART_ADD_FOR_ONE_GUESSED_WORD = 0.1f

        // Среднее количество букв в словах для расчета насколько сильно играет игрок
        const val AVERAGE_LETTERS_IN_WORD = 5f

        /**
         * Расчет статистики игроков
         */
        // Степень влияния результатов раунда игры на уровень отгадывания букв игрока
        const val LEVEL_RATIO = 0.2F

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

        // фон открытой карты с буквой
        const val IMAGE_CARD_FOREGROUND = "FACE.webp"

        // движение карусели
        const val SHIFT_ANIMATOR_PLAYER_NEXT_DP = 75f
        // время исчезновения игрока в карусели
        const val DURATION_ANIMATOR_NEXT_PLAYER = 350L

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

        /**
         * Настройки рейтинга
         */
        // суммарное количество карточек оранжевого и более сложных уровней для получения ранга Эксперт
        const val EXPERT_RATING = 50
        // суммарное количество карточек зеленого и более сложных уровней для получения ранга Мастер
        const val MASTER_RATING = 75
        // суммарное количество карточек голубого и более сложных уровней для получения ранга Гений
        const val GENIUS_RATING = 100
        // суммарное количество карточек фиолетового уровня для получения ранга Герой
        const val HERO_RATING = 125
        // количество карточек для всех цветов для получения ранга Легенда
        const val LEGEND_RATING = 150
        // количество отгаданных карточек определенного цвета, для получения следующей декорации (бронза, серебро и т.д.)
        const val DECORATION_RATING = 100

        /**
         * Настройки списка аватарок
         */
        // кол-во аватарок в одном ряду
        const val SPAN_COUNT_AVATARS_RECYCLER_VIEW = 4


        /**
         * DEBUG
         */
        // проверить файлы картинок
        const val DEBUG_IMAGE_FILES = true


    }
}