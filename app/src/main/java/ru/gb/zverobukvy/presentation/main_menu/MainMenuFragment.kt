package ru.gb.zverobukvy.presentation.main_menu

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.databinding.FragmentMainMenuBinding
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameFragment
import ru.gb.zverobukvy.presentation.animal_letters_game.AnimalLettersGameFragment.Companion.TAG_ANIMAL_LETTERS_FRAGMENT
import ru.gb.zverobukvy.presentation.main_menu.RemovePlayerDialogFragment.Companion.TAG_REMOVE_PLAYER_DIALOG_FRAGMENT
import ru.gb.zverobukvy.presentation.main_menu.list_avatars.AvatarsAdapter
import ru.gb.zverobukvy.presentation.main_menu.list_players.adapter.PlayersAdapter
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.gb.zverobukvy.presentation.main_menu.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf
import timber.log.Timber

class MainMenuFragment :
    ViewBindingFragment<FragmentMainMenuBinding>(FragmentMainMenuBinding::inflate) {

    private val viewModel: MainMenuViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            requireContext().appComponent.settingsScreenViewModel
        })[MainMenuViewModelImpl::class.java]
    }

    private val playersAdapter =
        PlayersAdapter(PlayerClickListenerOwner(::clickPlayer, ::clickEditMenuPlayer),
            EditPlayerClickListenerOwner(
                ::clickSaveChangedPlayer,
                ::clickCancelChangedPlayer,
                ::inputEditNameChangedPlayerClickListener,
                ::clickQueryRemovePlayer,
                ::clickAvatar
            ),
            AddPlayerClickListenerOwner { clickAddPlayer() })

    private val avatarsAdapter by lazy {
        AvatarsAdapter(::clickChangedAvatar, ::clickAddAvatars)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setRemovePlayerDialogFragmentListener()

        initView()
        viewModel.run {
            getLiveDataScreenState().observe(viewLifecycleOwner) {
                renderScreenState(it)
            }
            getLiveDataPlayersScreenState().observe(viewLifecycleOwner) {
                renderPlayersScreenState(it)
            }
            getLiveDataShowInstructionScreenState().observe(viewLifecycleOwner){
                renderShowInstructionScreenState()
            }
            getLiveDataAvatarsScreenState().observe(viewLifecycleOwner) {
                renderAvatarsScreenState(it)
            }
            onLaunch()
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
        initPlayersRecycleView()
        initAvatarsRecycleView()
        initPlayGameButton()
        initRoot()
        initShowInstructionImageView()
    }

    private fun initRoot() {
        binding.root.setOnClickListener {
            viewModel.onClickScreen()
        }
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

    private fun initPlayersRecycleView() {
        binding.playersRecyclerView.run {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = playersAdapter
        }
    }

    private fun initAvatarsRecycleView() {
        binding.avatarsRecyclerView.run {
            layoutManager = GridLayoutManager(
                requireContext(),
                SPAN_COUNT_AVATARS_RECYCLER_VIEW,
                RecyclerView.VERTICAL,
                false
            )
            adapter = avatarsAdapter
        }
    }

    private fun initShowInstructionImageView() {
        binding.showInstructionImageView.setOnClickListener {
            viewModel.onQueryShowInstruction()
        }
    }

    private fun renderScreenState(mainMenuState: MainMenuState.ScreenState) {
        when (mainMenuState) {
            is MainMenuState.ScreenState.ErrorState -> {
                Timber.d("ErrorState")
                showError(mainMenuState.error)
                binding.playButton.isClickable = true
            }

            is MainMenuState.ScreenState.StartGame -> {
                Timber.d("StartGame")
                openAnimalLettersFragment(
                    mainMenuState.typesCardsSelectedForGame,
                    mainMenuState.playersSelectedForGame
                )
            }

            is MainMenuState.ScreenState.TypesCardsState -> {
                Timber.d("TypesCardsState")
                initTypesCardsToggleButtons(
                    mainMenuState.typesCard
                )
            }
        }
    }

    private fun openAnimalLettersFragment(
        typesCardsSelectedForGame: List<TypeCards>, playersSelectedForGame: List<PlayerInGame>,
    ) {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.container, AnimalLettersGameFragment.newInstance(
                AnimalLettersGameFragment.GameStart(
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

    private fun renderPlayersScreenState(playersScreenState: MainMenuState.PlayersScreenState) {
        when (playersScreenState) {
            is MainMenuState.PlayersScreenState.AddPlayerState -> {
                Timber.d("AddPlayerState")
                onAddPlayer(
                    playersScreenState.playersInSettings, playersScreenState.positionAddPlayer
                )
            }

            is MainMenuState.PlayersScreenState.ChangedPlayerState -> {
                Timber.d("ChangedPlayerState")
                onChangedPlayer(
                    playersScreenState.playersInSettings, playersScreenState.positionChangedPlayer
                )
            }

            is MainMenuState.PlayersScreenState.PlayersState -> {
                Timber.d("PlayersState")
                onNewPlayers(playersScreenState.playersInSettings)
            }

            is MainMenuState.PlayersScreenState.RemovePlayerState -> {
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

    private fun clickSaveChangedPlayer() {
        viewModel.onChangedPlayer()
    }

    private fun clickCancelChangedPlayer() {
        viewModel.onCancelChangedPlayer()
    }

    private fun clickQueryRemovePlayer(position: Int) {
        RemovePlayerDialogFragment.newInstance().also {
            it.arguments = bundleOf(
                RemovePlayerDialogFragment.KEY_POSITION_REMOVE_PLAYER to position
            )
            it.show(requireActivity().supportFragmentManager, TAG_REMOVE_PLAYER_DIALOG_FRAGMENT)
        }
    }

    private fun inputEditNameChangedPlayerClickListener(name: String) {
        viewModel.onEditNamePlayer(name)
    }

    private fun clickAvatar() {
        viewModel.onClickAvatar()
    }

    private fun clickChangedAvatar(avatarPosition: Int) {
        viewModel.onQueryChangedAvatar(avatarPosition)
    }

    private fun clickAddAvatars(){
        viewModel.onQueryAddAvatars()
    }

    private fun clickAddPlayer() {
        viewModel.onAddPlayer()
    }

    private fun renderShowInstructionScreenState() {
        Timber.d("renderShowInstructionScreenState")
        parentFragmentManager.setFragmentResult(
            TAG_MAIN_MENU_FRAGMENT_SHOW_INSTRUCTIONS,
            bundleOf()
        )
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

    private fun renderAvatarsScreenState(avatarsScreenState: MainMenuState.AvatarsScreenState) {
        when (avatarsScreenState) {
            MainMenuState.AvatarsScreenState.HideAvatarsState -> {
                Timber.d("HideAvatarsState")
                binding.avatarsRecyclerViewLayout.visibility = View.GONE
            }

            is MainMenuState.AvatarsScreenState.ShowAvatarsState -> {
                Timber.d("ShowAvatarsState")
                binding.avatarsRecyclerViewLayout.visibility = View.VISIBLE
                avatarsAdapter.setAvatars(avatarsScreenState.avatars)
            }
        }
    }

    companion object {
        const val TAG_MAIN_MENU_FRAGMENT = "MainMenuFragment"
        const val TAG_MAIN_MENU_FRAGMENT_SHOW_INSTRUCTIONS = "MainMenuFragmentShowInstructions"
        const val KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT =
            "KeyResultFromRemovePlayerDialogFragment"
        const val SPAN_COUNT_AVATARS_RECYCLER_VIEW = 4

        @JvmStatic
        fun newInstance() = MainMenuFragment()

        fun setOnListenerShowInstruction(activity: AppCompatActivity, f: (() -> Unit)?) {
            activity.supportFragmentManager.setFragmentResultListener(
                TAG_MAIN_MENU_FRAGMENT_SHOW_INSTRUCTIONS,
                activity
            ) { _, _ ->
                f?.invoke()
            }
        }
    }
}