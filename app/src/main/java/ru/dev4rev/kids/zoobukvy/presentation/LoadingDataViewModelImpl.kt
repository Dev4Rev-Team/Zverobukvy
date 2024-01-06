package ru.dev4rev.kids.zoobukvy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.dev4rev.kids.zoobukvy.configuration.Conf
import ru.dev4rev.kids.zoobukvy.data.check_data.CheckData
import ru.dev4rev.kids.zoobukvy.data.theme_provider.Theme
import ru.dev4rev.kids.zoobukvy.data.theme_provider.ThemeProvider
import ru.dev4rev.kids.zoobukvy.domain.repository.LoadingDataRepository
import ru.dev4rev.kids.zoobukvy.presentation.customview.AssetsImageCash
import ru.dev4rev.kids.zoobukvy.presentation.sound.SoundEffectPlayer
import javax.inject.Inject
@Suppress("UNUSED_PARAMETER")
class LoadingDataViewModelImpl @Inject constructor(
    playersRepository: LoadingDataRepository,
    private val themeProvider: ThemeProvider,
    checkData: CheckData,
    assetsImageCash: AssetsImageCash,
    soundEffectPlayer: SoundEffectPlayer
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