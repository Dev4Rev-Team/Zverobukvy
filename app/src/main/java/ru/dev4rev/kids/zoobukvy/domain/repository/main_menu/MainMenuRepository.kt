package ru.dev4rev.kids.zoobukvy.domain.repository.main_menu

import ru.dev4rev.kids.zoobukvy.domain.repository.PlayersRepository
import ru.dev4rev.kids.zoobukvy.domain.repository.main_menu.shared_preferences.SharedPreferencesForGameRepository

interface MainMenuRepository : PlayersRepository, SharedPreferencesForGameRepository,
        AvatarsRepository, NetworkStatusRepository