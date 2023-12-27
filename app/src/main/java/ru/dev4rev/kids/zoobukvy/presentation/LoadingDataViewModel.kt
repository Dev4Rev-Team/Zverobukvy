package ru.dev4rev.kids.zoobukvy.presentation

import androidx.lifecycle.LiveData
import ru.dev4rev.kids.zoobukvy.data.theme_provider.Theme

interface LoadingDataViewModel {
    fun getLiveDataLoadingData(): LiveData<Boolean>

    fun getTheme(): Theme
}