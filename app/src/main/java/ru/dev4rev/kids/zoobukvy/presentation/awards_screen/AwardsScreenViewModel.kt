package ru.dev4rev.kids.zoobukvy.presentation.awards_screen

import androidx.lifecycle.LiveData

interface AwardsScreenViewModel {

    fun getMainAwardsLiveData(): LiveData<AwardsScreenState.Main>

    fun getSecondAwardsLiveData(): LiveData<AwardsScreenState.Second>

    fun onNextClick()
}