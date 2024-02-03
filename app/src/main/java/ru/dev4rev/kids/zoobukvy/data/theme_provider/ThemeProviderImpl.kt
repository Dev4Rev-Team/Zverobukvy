package ru.dev4rev.kids.zoobukvy.data.theme_provider

import ru.dev4rev.kids.zoobukvy.configuration.Conf
import java.util.Calendar
import javax.inject.Inject

class ThemeProviderImpl @Inject constructor() : ThemeProvider {

    override fun getTheme(): Theme {
        val currentDate = Calendar.getInstance()
        return if (currentDate > START_NEY_YEAR_PERIOD || currentDate <= END_NEY_YEAR_PERIOD)
            Theme.NEW_YEAR
        else if (currentDate > START_WINTER_PERIOD || currentDate <= START_SPRING_PERIOD)
            Theme.WINTER
        else if (currentDate > START_SPRING_PERIOD && currentDate <= START_SUMMER_PERIOD)
            Theme.SPRING
        else if (currentDate > START_SUMMER_PERIOD && currentDate <= START_AUTUMN_PERIOD)
            Theme.SUMMER
        else if (currentDate > START_AUTUMN_PERIOD && currentDate <= START_WINTER_PERIOD)
            Theme.AUTUMN
        else
            Theme.BASE
    }

    companion object {
        private val START_NEY_YEAR_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.START_NEY_YEAR_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.START_NEY_YEAR_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private val END_NEY_YEAR_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.END_NEY_YEAR_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.END_NEY_YEAR_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private val START_WINTER_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.START_WINTER_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.START_WINTER_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private val START_SPRING_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.START_SPRING_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.START_SPRING_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private val START_SUMMER_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.START_SUMMER_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.START_SUMMER_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }

        private val START_AUTUMN_PERIOD by lazy {
            Calendar.getInstance().apply {
                set(Calendar.MONTH, Conf.START_AUTUMN_PERIOD_MONTH)
                set(Calendar.DAY_OF_MONTH, Conf.START_AUTUMN_PERIOD_DAY)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
    }
}