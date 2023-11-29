package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.domain.repository.LoadingDataRepository
import javax.inject.Inject

class LoadingDataViewModelImpl @Inject constructor(playersRepository: LoadingDataRepository) :
    ViewModel(), LoadindDataViewModel {
    private val loadingDataLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            playersRepository.loadingData()
            loadingDataLiveData.value = true
        }
    }

    override fun getLiveDataLoadingData(): LiveData<Boolean> = loadingDataLiveData

}