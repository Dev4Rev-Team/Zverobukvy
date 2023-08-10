package ru.gb.zverobukvy.presentation

import androidx.lifecycle.LiveData
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.TypeCards

interface SettingsScreenViewModel {

    /**
     * Метод вызывается при создании или пересоздании view, до подписки на liveData.
     * В этом методе viewModel запрашивает список игроков в репозитории и при получении данных
     * формирует состояние Players. При пересоздании view, т.е. когда во viewModel уже хранится
     * список игроков, обращение в репозиторий не происходит.
     * @param typesCardsSelectedForGameFromPreference сохраненные настройки уровня игры (цвета игры),
     * которые извлекаются из preference (пустой список, если данные не сохранены)
     * @param namesPlayersSelectedForGameFromPreference сохраненные в настройках имена игроков,
     * выбранных для игры; извлекаются из preference (пустой список, если данные не сохранены)
     */
    fun onLaunch(
        typesCardsSelectedForGameFromPreference: List<TypeCards>,
        namesPlayersSelectedForGameFromPreference: List<String>
    )

    /**
    Метод для подписки view на состояние списка игроков на экране настроек.
     */
    fun getLiveDataPlayersScreenState(): LiveData<SettingsScreenState.PlayersScreenState>

    /**
    Метод для подписки viewна состояние экрана настроек.
     */
    fun getLiveDataScreenState(): SingleEventLiveData<SettingsScreenState.ScreenState>

    /**
     * Метод вызывается при выборе или отмене выбора игрока для участия в игре, например, по клику
     * на item игрока. ViewModel изменяет значение isSelectedForGame для данного игрока и формирует
     * состояние ChangedPlayer, по которому во view в адаптер передается новый список игроков и
     * изменяется выделение соответствующего item игрока.
     */
    fun onChangedSelectingPlayer(positionPlayer: Int)

    /**
     * Метод вызывается при удаление игрока: нажатие соответствующей кнопки на редактируемом
     * item игрока и последующее подтверждение в диалоговом окне.
     * ViewModel удаляет игрока из списка игроков, информирует об этом репозпитоий для внесения
     * изменений в БД и формирует состояние RemovePlayer, по которому во view в адаптер передается
     * новый список игроков и удаляется соответствующий item игрока.
     */
    fun onRemovePlayer(positionPlayer: Int)

    /**
     * Метод вызывается при запросе на изменение данных игрока, например при нажатии на кнопку
     * "редактирование" в item игрока. ViewModel изменяет значение inEditingState = true соответствующего
     * игрока и формирует состояние ChangedPlayer, по которому во view в адаптер передается новый
     * список игроков и изменяется соответствующий item игрока: переводится в режим редактирования.
     */
    fun onQueryChangedPlayer(positionPlayer: Int)

    /**
     * Метод вызывается при изменении данных игрока, например, при нажатии ОК в item игрока в режиме
     * редактирования этого item (на данном этапе изменяется только имя). ViewModel изменяет значение
     * name соответствующего игрока, информирует об этом репозиторий для внесения изменений в БД и
     * формирует состояние ChangedPlayer, по которому во view в адаптер передается новый
     * список игроков и изменяется соответствующий item игрока.
     * Если введенное имя некорректно или повторяется ViewModel формирует состояние Error, по которому
     * во view соответствующим образом информируется пользователь, например Toast.
     */
    fun onChangedPlayer(positionPlayer: Int, newNamePlayer: String)

    /**
     * Метод вызывается при отказе пользователя в редактировании данных игрока, например при нажатии
     * Cancel в item игрока. ViewModel изменяет значение inEditingState = false соответствующего
     * игрока и формирует состояние ChangedPlayer, по которому во view в адаптер передается новый
     * список игроков и изменяется соответствующий item игрока: переводится в нередактируемый режим.
     */
    fun onCancelChangedPlayer(positionPlayer: Int)

    /**
     * Метод вызывается при добавление нового игрока, например при нажатии на кнопку "+".
     * ViewModel добавляет в список "дефолтного" игрока, отправляет соответствующую информацию в
     * репозиторий для внесения изменений в БД и формирует состояние AddPlayer, по которому во view
     * в адаптер передается новый список игроков и добавляется item нового игрока.
     * Редактирование и удаление нового игрока осуществляется аналогично существующим игрокам.
     */
    fun onAddPlayer()

    /**
     * Метод вызывается при клике на уровень игры (цвет игры), например на элемент chekbox.
     * ViewModel сохраняет соответствующие изменения в список уровней игры. View самостоятельно
     * отображает выбранный/невыбранный элемент chekbox.
     */
    fun onClickTypeCards(typeCards: TypeCards)

    /**
     * Метод вызывается при нажатии кнопки "Начало игры". ViewModel формирует состояние StartGame,
     * по которому view открывает экран игры "Зверобуквы" и передает список игроков и список
     * уровня игры (цвета игры).
     * Если не были выбраны игроки или уровень игры, то ViewModel формирует состояние Error, по
     * которому во view соответствующим образом информируется пользователь, например Toast.
     */
    fun onStartGame()

}