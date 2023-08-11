package ru.gb.zverobukvy.presentation.main_menu.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.data.data_source_impl.LetterCardsDBImpl
import ru.gb.zverobukvy.data.data_source_impl.WordCardsDBImpl
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersCardsRepositoryImpl
import ru.gb.zverobukvy.databinding.FragmentMainMenuBinding
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.PlayersRepository
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyFragment
import ru.gb.zverobukvy.presentation.main_menu.preferences.SharedPreferencesForGameImpl
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.adapter.PlayersAdapter
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.view.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModel
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModelImpl
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf

class MainMenuFragment :
    ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {
    private val viewModel: SettingsScreenViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            val playersRepository: PlayersRepository =
                AnimalLettersCardsRepositoryImpl(LetterCardsDBImpl(), WordCardsDBImpl())
            SettingsScreenViewModelImpl(playersRepository)
        })[SettingsScreenViewModelImpl::class.java]
    }

    private val sharedPreferencesForGame: SharedPreferencesForGameImpl =
        SharedPreferencesForGameImpl(requireActivity())

    private val playersAdapter = PlayersAdapter(
        PlayerClickListenerOwner(::clickPlayer, ::clickEditMenuPlayer),
        EditPlayerClickListenerOwner(
            ::clickSaveChangedPlayer,
            ::clickCancelChangedPlayer,
            ::clickQueryRemovePlayer
        ),
        AddPlayerClickListenerOwner { clickAddPlayer() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typesCardsSelectedForGame = sharedPreferencesForGame.readTypesCardsSelectedForGame()
        val namesPlayersSelectedForGame = sharedPreferencesForGame.readNamesPlayersSelectedForGame()
        initView(typesCardsSelectedForGame)
        viewModel.run {
            onLaunch(
                typesCardsSelectedForGame,
                namesPlayersSelectedForGame
            )
            getLiveDataScreenState().observe(viewLifecycleOwner) {
                renderSettingsScreenState(it)
            }
            getLiveDataPlayersScreenState().observe(viewLifecycleOwner) {
                renderPlayersScreenState(it)
            }
        }
    }

    private fun initView(typesCardsSelectedForGame: List<TypeCards>) {
        initRecycleView()
        initTypesCardsToggleButtons(typesCardsSelectedForGame)
        initPlayGameButton()
    }

    private fun initTypesCardsToggleButtons(typesCardsSelectedForGame: List<TypeCards>) {
        binding.run {
            initTypeCardToggleButton(
                fragmentMainMenuToggleButtonOrange,
                TypeCards.ORANGE,
                typesCardsSelectedForGame.contains(TypeCards.ORANGE)
            )
            initTypeCardToggleButton(
                fragmentMainMenuToggleButtonGreen,
                TypeCards.GREEN,
                typesCardsSelectedForGame.contains(TypeCards.GREEN)
            )
            initTypeCardToggleButton(
                fragmentMainMenuToggleButtonBlue,
                TypeCards.BLUE,
                typesCardsSelectedForGame.contains(TypeCards.BLUE)
            )
            initTypeCardToggleButton(
                fragmentMainMenuToggleButtonViolet,
                TypeCards.VIOLET,
                typesCardsSelectedForGame.contains(TypeCards.VIOLET)
            )
        }
    }

    private fun initTypeCardToggleButton(
        toggleButton: ToggleButton,
        typeCard: TypeCards,
        isChecked: Boolean
    ) {
        toggleButton.apply {
            check(isChecked)
            setOnCheckedChangeListener { _, _ ->
                viewModel.onClickTypeCards(typeCard)
            }
        }
    }

    private fun initPlayGameButton() {
        viewModel.onStartGame()
    }

    private fun initRecycleView() {
        binding.fragmentMainMenuRecyclerViewPlayers.run {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = playersAdapter
        }
    }

    private fun renderSettingsScreenState(settingsScreenState: SettingsScreenState.ScreenState) {
        when (settingsScreenState) {
            is SettingsScreenState.ScreenState.ErrorState -> showError(settingsScreenState.error)
            is SettingsScreenState.ScreenState.StartGame -> openAnimalLettersFragment(
                settingsScreenState.typesCardsSelectedForGame,
                settingsScreenState.playersSelectedForGame
            )
        }
    }

    private fun openAnimalLettersFragment(
        typesCardsSelectedForGame: List<TypeCards>,
        playersSelectedForGame: List<PlayerInGame>
    ) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(
                R.id.container,
                GameZverobukvyFragment.newInstance(
                    GameZverobukvyFragment.GameStart(
                        typesCardsSelectedForGame,
                        playersSelectedForGame
                    )
                ),
                TAG_ANIMAL_LETTERS_FRAGMENT
            )
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun showError(error: String) {
        Toast.makeText(
            requireContext(),
            error,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun renderPlayersScreenState(playersScreenState: SettingsScreenState.PlayersScreenState) {
        when (playersScreenState) {
            is SettingsScreenState.PlayersScreenState.AddPlayerState -> playersAdapter.addPlayer(
                playersScreenState.playersInSettings,
                playersScreenState.positionAddPlayer
            )

            is SettingsScreenState.PlayersScreenState.ChangedPlayerState -> playersAdapter.changedPlayer(
                playersScreenState.playersInSettings,
                playersScreenState.positionChangedPlayer
            )

            is SettingsScreenState.PlayersScreenState.PlayersState -> playersAdapter.setNewPlayers(
                playersScreenState.playersInSettings
            )

            is SettingsScreenState.PlayersScreenState.RemovePlayerState -> playersAdapter.removePlayer(
                playersScreenState.playersInSettings,
                playersScreenState.positionRemovePlayer
            )
        }
    }

    private fun clickPlayer(position: Int) {
        viewModel.onChangedSelectingPlayer(position)
    }

    private fun clickEditMenuPlayer(position: Int) {
        viewModel.onQueryChangedPlayer(position)
    }

    private fun clickSaveChangedPlayer(position: Int, newName: String) {
        viewModel.onChangedPlayer(position, newName)
    }

    private fun clickCancelChangedPlayer(position: Int) {
        viewModel.onCancelChangedPlayer(position)
    }

    private fun clickQueryRemovePlayer(position: Int, name: String) {
        RemovePlayerDialogFragment {
            viewModel.onRemovePlayer(position)
        }.also {
            it.arguments = Bundle().apply {
                putString(RemovePlayerDialogFragment.KEY_NAME_PLAYER, name)
            }
            it.show(requireActivity().supportFragmentManager, TAG_REMOVE_PLAYER_DIALOG_FRAGMENT)
        }
    }

    private fun clickAddPlayer() {
        viewModel.onAddPlayer()
    }

    companion object {
        private const val TAG_ANIMAL_LETTERS_FRAGMENT = "AnimalLettersFragment"
        private const val TAG_REMOVE_PLAYER_DIALOG_FRAGMENT = "RemovePlayerDialogFragment"

        @JvmStatic
        fun newInstance() =
            MainMenuFragment()
    }
}