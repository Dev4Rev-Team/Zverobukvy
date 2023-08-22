package ru.gb.zverobukvy.presentation.main_menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.App
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.data.resources_provider.ResourcesProviderImpl
import ru.gb.zverobukvy.databinding.FragmentMainMenuBinding
import ru.gb.zverobukvy.domain.app_state.SettingsScreenState
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.PlayerInSettings
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.repository.MainMenuRepository
import ru.gb.zverobukvy.presentation.game_zverobukvy.GameZverobukvyFragment
import ru.gb.zverobukvy.presentation.main_menu.list_players.adapter.PlayersAdapter
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModel
import ru.gb.zverobukvy.presentation.main_menu.viewModel.SettingsScreenViewModelImpl
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf
import timber.log.Timber

class MainMenuFragment :
    ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {
    private val viewModel: SettingsScreenViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            val repository: MainMenuRepository =
                (requireContext().applicationContext as App).mainMenuRepository
            val resourcesProvider = ResourcesProviderImpl(requireContext())
            SettingsScreenViewModelImpl(repository, resourcesProvider)
        })[SettingsScreenViewModelImpl::class.java]
    }

    private val playersAdapter =
        PlayersAdapter(PlayerClickListenerOwner(::clickPlayer, ::clickEditMenuPlayer),
            EditPlayerClickListenerOwner(
                ::clickSaveChangedPlayer, ::clickCancelChangedPlayer, ::clickQueryRemovePlayer
            ),
            AddPlayerClickListenerOwner { clickAddPlayer() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setRemovePlayerDialogFragmentListener()

        initView()
        viewModel.run {
            onLaunch()
            getLiveDataScreenState().observe(viewLifecycleOwner) {
                renderSettingsScreenState(it)
            }
            getLiveDataPlayersScreenState().observe(viewLifecycleOwner) {
                renderPlayersScreenState(it)
            }
        }
    }

    override fun onPause() {
        Timber.d("onPause")
        viewModel.onViewPause()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        Timber.d("onBackPressed")
        requireActivity().finish()
        return super.onBackPressed()
    }

    private fun initView() {
        initRecycleView()
        initPlayGameButton()
    }

    private fun initTypesCardsToggleButtons(typesCardsSelectedForGame: List<TypeCards>) {
        binding.run {
            initTypeCardToggleButton(
                orangeToggleButton,
                TypeCards.ORANGE,
                typesCardsSelectedForGame.contains(TypeCards.ORANGE)
            )
            initTypeCardToggleButton(
                greenToggleButton,
                TypeCards.GREEN,
                typesCardsSelectedForGame.contains(TypeCards.GREEN)
            )
            initTypeCardToggleButton(
                blueToggleButton, TypeCards.BLUE, typesCardsSelectedForGame.contains(TypeCards.BLUE)
            )
            initTypeCardToggleButton(
                violetToggleButton,
                TypeCards.VIOLET,
                typesCardsSelectedForGame.contains(TypeCards.VIOLET)
            )
        }
    }

    private fun initTypeCardToggleButton(
        toggleButton: ToggleButton, typeCard: TypeCards, isChecked: Boolean,
    ) {
        toggleButton.apply {
            setChecked(isChecked)
            setOnCheckedChangeListener { _, _ ->
                viewModel.onClickTypeCards(typeCard)
            }
        }
    }

    private fun initPlayGameButton() {
        binding.playButton.apply {
            isClickable = true
            setOnClickListener {
                isClickable = false
                viewModel.onStartGame()
            }
        }
    }

    private fun initRecycleView() {
        binding.playersRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = playersAdapter
        }
    }

    private fun renderSettingsScreenState(settingsScreenState: SettingsScreenState.ScreenState) {
        when (settingsScreenState) {
            is SettingsScreenState.ScreenState.ErrorState -> {
                Timber.d("ErrorState")
                showError(settingsScreenState.error)
                binding.playButton.isClickable = true
            }

            is SettingsScreenState.ScreenState.StartGame -> {
                Timber.d("StartGame")
                openAnimalLettersFragment(
                    settingsScreenState.typesCardsSelectedForGame,
                    settingsScreenState.playersSelectedForGame
                )
            }

            is SettingsScreenState.ScreenState.TypesCardsState -> {
                Timber.d("TypesCardsState")
                initTypesCardsToggleButtons(
                    settingsScreenState.typesCard
                )
            }
        }
    }

    private fun openAnimalLettersFragment(
        typesCardsSelectedForGame: List<TypeCards>, playersSelectedForGame: List<PlayerInGame>,
    ) {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter_x, R.anim.exit_x, R.anim.enter_x, R.anim.exit_x)
            .replace(
            R.id.container, GameZverobukvyFragment.newInstance(
                GameZverobukvyFragment.GameStart(
                    typesCardsSelectedForGame, playersSelectedForGame
                )
            ), TAG_ANIMAL_LETTERS_FRAGMENT
        ).addToBackStack(null).commitAllowingStateLoss()
    }

    private fun showError(error: String) {
        Toast.makeText(
            requireContext(), error, Toast.LENGTH_LONG
        ).show()
    }

    private fun renderPlayersScreenState(playersScreenState: SettingsScreenState.PlayersScreenState) {
        when (playersScreenState) {
            is SettingsScreenState.PlayersScreenState.AddPlayerState -> {
                Timber.d("AddPlayerState")
                onAddPlayer(
                    playersScreenState.playersInSettings, playersScreenState.positionAddPlayer
                )
            }

            is SettingsScreenState.PlayersScreenState.ChangedPlayerState -> {
                Timber.d("ChangedPlayerState")
                onChangedPlayer(
                    playersScreenState.playersInSettings, playersScreenState.positionChangedPlayer
                )
            }

            is SettingsScreenState.PlayersScreenState.PlayersState -> {
                Timber.d("PlayersState")
                onNewPlayers(playersScreenState.playersInSettings)
            }

            is SettingsScreenState.PlayersScreenState.RemovePlayerState -> {
                Timber.d("RemovePlayerState")
                onRemovePlayer(
                    playersScreenState.playersInSettings, playersScreenState.positionRemovePlayer
                )
            }
        }
    }

    private fun onChangedPlayer(newPlayers: List<PlayerInSettings?>, positionChangedPlayer: Int) {
        playersAdapter.changedPlayer(newPlayers, positionChangedPlayer)
    }

    private fun onAddPlayer(newPlayers: List<PlayerInSettings?>, positionAddPlayer: Int) {
        playersAdapter.addPlayer(newPlayers, positionAddPlayer)
        with(binding.playersRecyclerView) {
            adapter?.let {
                scrollToPosition(it.itemCount - 1)
            }
        }
    }

    private fun onRemovePlayer(newPlayers: List<PlayerInSettings?>, positionRemovePlayer: Int) {
        playersAdapter.removePlayer(newPlayers, positionRemovePlayer)
    }

    private fun onNewPlayers(newPlayers: List<PlayerInSettings?>) {
        playersAdapter.setNewPlayers(newPlayers)
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
        RemovePlayerDialogFragment().also {
            it.arguments = bundleOf(
                RemovePlayerDialogFragment.KEY_NAME_PLAYER to name,
                RemovePlayerDialogFragment.KEY_POSITION_REMOVE_PLAYER to position
            )
            it.show(requireActivity().supportFragmentManager, TAG_REMOVE_PLAYER_DIALOG_FRAGMENT)
        }
    }

    private fun clickAddPlayer() {
        viewModel.onAddPlayer()
    }

    private fun setRemovePlayerDialogFragmentListener() {
        requireActivity().supportFragmentManager.setFragmentResultListener(
            KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT, viewLifecycleOwner
        ) { requestKey, result ->
            if (requestKey == KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT) viewModel.onRemovePlayer(
                result.getInt(RemovePlayerDialogFragment.KEY_POSITION_REMOVE_PLAYER)
            )
        }
    }

    companion object {
        private const val TAG_ANIMAL_LETTERS_FRAGMENT = "GameAnimalLettersFragment"
        private const val TAG_REMOVE_PLAYER_DIALOG_FRAGMENT = "RemovePlayerDialogFragment"
        const val TAG_MAIN_MENU_FRAGMENT = "MainMenuFragment"
        const val KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT =
            "KeyResultFromRemovePlayerDialogFragment"

        @JvmStatic
        fun newInstance() = MainMenuFragment()
    }
}