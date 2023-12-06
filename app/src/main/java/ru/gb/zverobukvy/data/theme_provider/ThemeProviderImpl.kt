package ru.gb.zverobukvy.data.theme_provider

import ru.gb.zverobukvy.configuration.Conf
import java.util.Calendar
import javax.inject.Inject

class ThemeProviderImpl @Inject constructor() : ThemeProvider {

    override fun getTheme(): Theme {
        val currentDate = Calendar.getInstance()
        return if (currentDate > START_NEY_YEAR_PERIOD || currentDate < END_NEY_YEAR_PERIOD)
            Theme.NEW_YEAR
        else
            Theme.STANDARD

    }

    companion object {
        private val START_NEY_YEAR_PERIOD = Calendar.getInstance().apply {
            set(Calendar.MONTH, Conf.START_NEY_YEAR_PERIOD_MONTH)
            set(Calendar.DAY_OF_MONTH, Conf.START_NEY_YEAR_PERIOD_DAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }
        private val END_NEY_YEAR_PERIOD = Calendar.getInstance().apply {
            set(Calendar.MONTH, Conf.END_NEY_YEAR_PERIOD_MONTH)
            set(Calendar.DAY_OF_MONTH, Conf.END_NEY_YEAR_PERIOD_DAY)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }
    }
}