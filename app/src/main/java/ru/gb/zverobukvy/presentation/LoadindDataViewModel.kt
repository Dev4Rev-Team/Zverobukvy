package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData

interface LoadindDataViewModel {
    fun getLiveDataLoadingData(): LiveData<Boolean>
}