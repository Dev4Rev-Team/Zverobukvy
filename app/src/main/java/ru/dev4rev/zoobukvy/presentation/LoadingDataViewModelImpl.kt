package ru.dev4rev.zoobukvy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dev4rev.zoobukvy.configuration.Conf
import ru.dev4rev.zoobukvy.data.check_data.CheckData
import ru.dev4rev.zoobukvy.data.theme_provider.Theme
import ru.dev4rev.zoobukvy.data.theme_provider.ThemeProvider
import ru.dev4rev.zoobukvy.domain.repository.LoadingDataRepository
import javax.inject.Inject

class LoadingDataViewModelImpl @Inject constructor(
    playersRepository: LoadingDataRepository,
    private val themeProvider: ThemeProvider,
    checkData: CheckData
) :
    ViewModel(), LoadingDataViewModel {
    private val loadingDataLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {
            playersRepository.loadingData()
            loadingDataLiveData.value = true
            // проверка данных в БД
            if (Conf.IS_CHECK_DATA)
                checkData.checkData()
        }
    }

    override fun getLiveDataLoadingData(): LiveData<Boolean> = loadingDataLiveData
    override fun getTheme(): Theme = themeProvider.getTheme()
}