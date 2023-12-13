package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.data.theme_provider.Theme

interface LoadingDataViewModel {
    fun getLiveDataLoadingData(): LiveData<Boolean>

    fun getTheme(): Theme
}