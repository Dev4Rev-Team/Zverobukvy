package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gb.zverobukvy.BuildConfig
import ru.gb.zverobukvy.configuration.Conf
import ru.gb.zverobukvy.domain.repository.LoadingDataRepository
import ru.gb.zverobukvy.data.check_data.CheckData
import javax.inject.Inject

class LoadingDataViewModelImpl @Inject constructor(
    playersRepository: LoadingDataRepository,
    checkData: CheckData
) :
    ViewModel(), LoadingDataViewModel {
    private val loadingDataLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            playersRepository.loadingData()
            loadingDataLiveData.value = true
            // проверка данных в БД
            if (BuildConfig.DEBUG && Conf.IS_CHECK_DATA)
                checkData.checkData()
        }
    }

    override fun getLiveDataLoadingData(): LiveData<Boolean> = loadingDataLiveData
}