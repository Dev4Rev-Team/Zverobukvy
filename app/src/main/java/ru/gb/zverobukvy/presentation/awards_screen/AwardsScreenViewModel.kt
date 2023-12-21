package ru.gb.zverobukvy.presentation.awards_screen

import androidx.lifecycle.LiveData

interface AwardsScreenViewModel {

    fun getMainAwardsLiveData(): LiveData<AwardsScreenState.Main>

    fun getSecondAwardsLiveData(): LiveData<AwardsScreenState.Second>

    fun onNextClick()
}