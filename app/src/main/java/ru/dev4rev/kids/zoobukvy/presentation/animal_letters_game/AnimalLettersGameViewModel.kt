package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game

import androidx.lifecycle.LiveData
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus

/**
Во viewModel необходимо хранить текущее полное состояние игры (var currentGameState: GameState?).
При получении от интерактора нового полного состояния игры, viewmodel сравнивает текущее состояние
с новым, формирует и заносит соответствующие данные в сhangingGameStateLiveData и/или
entireStateLiveData. После этого обновляет текущее полное состояние игры (currentGameState).
Т.е. во viewModel должна быть реализована логика преобразования данных, полученных от интерактора,
в данные необходимые для view.
 */
interface AnimalLettersGameViewModel {
    /**
    Метод вызывается при создании или при пересоздании view, в этом методе viewModel присваивает
    entireStateLiveData полное состояние игры (все данные для полной отрисовки экрана игры).
     */
    fun onActiveGame()

    /**
    Метод для подписки view на полное состояние игры.
    При подписке view получает текущее полное состояние игры.
     */
    fun getEntireGameStateLiveData(): LiveData<AnimalLettersGameState.EntireState>

    /**
    Методы для подписки view на изменения состояния игры по ходу игры.
    Непосредственно при подписке view ничего получать не будет, т.к. реализовывается подход SingleEvent.
     */
    fun getChangingGameStateLiveData(): LiveData<AnimalLettersGameState.ChangingState>

    /**
     * Метод для подписки view на состояния звука (on/off)
     */
    fun getSoundStatusLiveData(): LiveData<Boolean>

    /**
     * Метод для подписки view на состояние озвучки букв (буквы или звуки или отключено)
     */
    fun getVoiceActingStatusLiveData(): LiveData<VoiceActingStatus>

    /**
     * Метод вызывается при нажатии на элемент отключения/включения звука
     */
    fun onSoundClick()

    /**
     * Метод вызывается при нажатии на элемент переключения озвучки букв
     */
    fun onVoiceActingClick()

    /**
    Метод вызывается при выборе буквенной карточки, в этом методе viewModel вызывает
    метод интерактора selectionLetterCard() и после получения ответа присваивает
    сhangingGameStateLiveData изменения состояния игры (реакция на выбор буквенной карточки)
     */
    fun onClickLetterCard(positionSelectedLetterCard: Int)

    /**
    Метод вызывается при нажатии пользователем кнопки Back, в этом методе viewModel (без обращения
    к интерактору) присваивает changingGameStateLiveData состояние, соответствующее открытию
    диалогового окна. При дальнейшем нажатии OK - view вызывает метод viewModel.onEndGameByUser(),
    при нажатии Cancel - view закрывает диалоговое окно и уведомляет об этом viewModel
    посредством вызова [onLoadGame].
     */
    fun onBackPressed()

    /**
    Метод вызывается при закрытии диалогового окна о прекращении игры пользователем, никаких
    новых состояний viewModel не отправляет.
     */
    fun onLoadGame()

    /**
    Метод вызывается при завершении игры пользователем, в этом методе viewModel вызывает метод
    интерактора endGameByUser() и после получения ответа присваивает
    entireGameStateLiveData состояние игры - завершение игры.
     */
    fun onEndGameByUser()

    /**
     Метод вызывается когда игра выходит из состояния паузы, никаких новых состояний viewModel не отправляет.

     * @see onPause
     */
    fun onResume()

    /**
    Метод вызывается, когда игра становится на паузу, никаких новых состояний viewModel не отправляет.

     * @see onResume
     */
    fun onPause()
}