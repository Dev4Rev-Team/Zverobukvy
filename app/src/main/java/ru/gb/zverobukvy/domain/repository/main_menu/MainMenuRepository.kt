package ru.gb.zverobukvy.domain.repository.main_menu

import ru.gb.zverobukvy.domain.repository.PlayersRepository
import ru.gb.zverobukvy.domain.repository.main_menu.shared_preferences.SharedPreferencesForGameRepository

interface MainMenuRepository : PlayersRepository, SharedPreferencesForGameRepository,
        AvatarsRepository, NetworkStatusRepository