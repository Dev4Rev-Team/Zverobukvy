package ru.gb.zverobukvy.presentation.main_menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.databinding.FragmentMainMenuBinding
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.SettingsScreenViewModel
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyFragment
import ru.gb.zverobukvy.presentation.main_menu.list_players.adapter.PlayersAdapter
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment

class MainMenuFragment :
    ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {
    //TODO реализовать инициализацию viewModel
    private lateinit var viewModel: SettingsScreenViewModel
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
                fragmentChooseGameModeToggleButtonOrange,
                TypeCards.ORANGE,
                typesCardsSelectedForGame.contains(TypeCards.ORANGE)
            )
            initTypeCardToggleButton(
                fragmentChooseGameModeToggleButtonGreen,
                TypeCards.GREEN,
                typesCardsSelectedForGame.contains(TypeCards.GREEN)
            )
            initTypeCardToggleButton(
                fragmentChooseGameModeToggleButtonBlue,
                TypeCards.BLUE,
                typesCardsSelectedForGame.contains(TypeCards.BLUE)
            )
            initTypeCardToggleButton(
                fragmentChooseGameModeToggleButtonViolet,
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
        binding.playersRecyclerView.run {
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

            is SettingsScreenState.PlayersScreenState.PlayersState -> playersAdapter.setPlayers(
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
        //TODO alertDialog на удаление игрока
    }

    private fun clickAddPlayer() {
        viewModel.onAddPlayer()
    }

    companion object {
        private const val TAG_ANIMAL_LETTERS_FRAGMENT = "AnimalLettersFragment"

        @JvmStatic
        fun newInstance() =
            MainMenuFragment()
    }
}