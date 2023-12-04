package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData

interface LoadingDataViewModel {
    fun getLiveDataLoadingData(): LiveData<Boolean>
}