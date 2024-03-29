package ru.dev4rev.kids.zoobukvy.presentation.main_menu

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.configuration.Conf
import ru.dev4rev.kids.zoobukvy.configuration.Conf.Companion.SPAN_COUNT_AVATARS_RECYCLER_VIEW
import ru.dev4rev.kids.zoobukvy.databinding.FragmentMainMenuBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.presentation.InstructionBottomSheetDialogFragment
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.AnimalLettersGameFragment
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.AnimalLettersGameFragment.Companion.TAG_ANIMAL_LETTERS_FRAGMENT
import ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator.CustomAnimatorSoaring
import ru.dev4rev.kids.zoobukvy.presentation.customview.createAlphaShowAnimation
import ru.dev4rev.kids.zoobukvy.presentation.customview.createScaleAnimation
import ru.dev4rev.kids.zoobukvy.presentation.customview.createSwayAnimation
import ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator.CustomAnimatorSync
import ru.dev4rev.kids.zoobukvy.presentation.customview.custom_animator.CustomAnimatorSyncImpl
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.RemovePlayerDialogFragment.Companion.TAG_REMOVE_PLAYER_DIALOG_FRAGMENT
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_avatars.AvatarsAdapter
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.adapter.PlayersAdapter
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.click_listener_owner.AddPlayerClickListenerOwner
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.click_listener_owner.EditPlayerClickListenerOwner
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.list_players.click_listener_owner.PlayerClickListenerOwner
import ru.dev4rev.kids.zoobukvy.utility.ui.ViewBindingFragment
import ru.dev4rev.kids.zoobukvy.utility.ui.WrapContentLinearLayoutManager
import ru.dev4rev.kids.zoobukvy.utility.ui.viewModelProviderFactoryOf
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

    private val snackbar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE).apply {
            setAction(getString(R.string.ok)) { dismiss() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setRemovePlayerDialogFragmentListener()

        initView()
        viewModel.run {
            getLiveDataScreenState().observe(viewLifecycleOwner) {
                hideKeyboard()
                renderScreenState(it)
            }
            getLiveDataPlayersScreenState().observe(viewLifecycleOwner) {
                hideKeyboard()
                renderPlayersScreenState(it)
            }
            getLiveDataShowInstructionScreenState().observe(viewLifecycleOwner) {
                hideKeyboard()
                renderShowInstructionScreenState()
            }
            getLiveDataAvatarsScreenState().observe(viewLifecycleOwner) {
                hideKeyboard()
                renderAvatarsScreenState(it)
            }
            onLaunch()
        }
    }

    override fun onPause() {
        Timber.d("onPause")
        viewModel.onViewPause()
        animator.end()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        Timber.d("onBackPressed")
        if (snackbar.isShown)
            hideError()
        else
            viewModel.onBackPressed()
        return false
    }

    private fun initView() {
        initPlayersRecycleView()
        initAvatarsRecycleView()
        initPlayGameButton()
        initRoot()
        initShowBee()
        initInstructionsImageView()
    }

    private fun initRoot() {
        binding.root.setOnClickListener {
            hideError()
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
                hideError()
                viewModel.onClickTypeCards(typeCard)
            }
        }
    }

    private fun initPlayGameButton() {
        binding.playButton.apply {
            isClickable = true
            setOnClickListener {
                hideError()
                isClickable = false
                viewModel.onStartGame()
            }
        }
    }

    private fun initPlayersRecycleView() {
        binding.playersRecyclerView.run {
            layoutManager =
                WrapContentLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
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
        binding.closeAvatarRecyclerViewLayout.setOnClickListener {
            // закрываем список аватарок через метод клика на свободную область экрана
            viewModel.onClickScreen()
        }
    }

    private fun initInstructionsImageView() {
        binding.instructionsImageView.setOnClickListener {
            hideError()
            viewModel.onQueryShowInstruction()
        }
        InstructionBottomSheetDialogFragment.setOnListenerCloseAnimation(fragment = this) {
            animator.startSwayTable()
        }
    }

    private fun initShowBee() {
        binding.showBee.setOnClickListener {
            hideError()
            animator.startShowBee()
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
                openAnimalLettersGameFragment(
                    mainMenuState.typesCardsSelectedForGame,
                    mainMenuState.playersSelectedForGame
                )
            }

            is MainMenuState.ScreenState.TypesCardsState -> {
                Timber.d("TypesCardsState")
                initTypesCardsToggleButtons(
                    mainMenuState.typesCard
                )
                animator.changeSelectedCard(mainMenuState.typesCard)
            }

            MainMenuState.ScreenState.CloseAppState -> {
                Timber.d("CloseAppState")
                requireActivity().finish()
            }
        }
    }

    private fun openAnimalLettersGameFragment(
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
        if (!snackbar.isShown)
            snackbar.setText(error).show()
    }

    private fun hideError() {
        if (snackbar.isShown)
            snackbar.dismiss()
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
        hideError()
        viewModel.onChangedSelectingPlayer(position)
    }

    private fun clickEditMenuPlayer(position: Int) {
        hideError()
        viewModel.onQueryChangedPlayer(position)
    }

    private fun clickSaveChangedPlayer() {
        hideError()
        viewModel.onChangedPlayer()
    }

    private fun clickCancelChangedPlayer() {
        hideError()
        viewModel.onCancelChangedPlayer()
    }

    private fun clickQueryRemovePlayer(position: Int) {
        hideError()
        // проверяем, что диалог-фрагмент не существует (защита от двойного нажатия на удаление игрока)
        if (requireActivity().supportFragmentManager.findFragmentByTag(
                TAG_REMOVE_PLAYER_DIALOG_FRAGMENT
            ) == null
        )
            RemovePlayerDialogFragment.newInstance().also {
                it.arguments = bundleOf(
                    RemovePlayerDialogFragment.KEY_POSITION_REMOVE_PLAYER to position
                )
                it.show(requireActivity().supportFragmentManager, TAG_REMOVE_PLAYER_DIALOG_FRAGMENT)
            }
    }

    private fun inputEditNameChangedPlayerClickListener(name: String) {
        hideError()
        viewModel.onEditNamePlayer(name)
    }

    private fun clickAvatar() {
        hideError()
        hideKeyboard()
        viewModel.onClickAvatar()
    }

    private fun clickChangedAvatar(avatarPosition: Int) {
        hideError()
        viewModel.onQueryChangedAvatar(avatarPosition)
    }

    private fun clickAddAvatars() {
        hideError()
        viewModel.onQueryAddAvatars()
    }

    private fun clickAddPlayer() {
        hideError()
        viewModel.onAddPlayer()
    }

    private fun renderShowInstructionScreenState() {
        Timber.d("renderShowInstructionScreenState")
        InstructionBottomSheetDialogFragment.show(fragment = this)
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
                binding.avatarsRecyclerViewGroup.visibility = View.GONE
                avatarsAdapter.resetAvatars()
            }

            is MainMenuState.AvatarsScreenState.ShowAvatarsState -> {
                Timber.d("ShowAvatarsState")
                binding.avatarsRecyclerViewGroup.visibility = View.VISIBLE
                avatarsAdapter.setAvatars(avatarsScreenState.avatars)
                binding.avatarsRecyclerView.scrollToPosition(avatarsScreenState.scrollPosition)
            }
        }
    }

    private fun hideKeyboard() {
        requireActivity().currentFocus?.let {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private val animator = object {
        private var showBee: AnimatorSet? = null
        private var swayTable: ObjectAnimator? = null
        private var selectCard: MutableMap<TypeCards, CustomAnimatorSoaring?> = mutableMapOf()
        fun viewCard(typeCard: TypeCards): ToggleButton {
            val toggleButton = when (typeCard) {
                TypeCards.ORANGE -> binding.orangeToggleButton
                TypeCards.GREEN -> binding.greenToggleButton
                TypeCards.BLUE -> binding.blueToggleButton
                TypeCards.VIOLET -> binding.violetToggleButton
            }
            return toggleButton
        }

        private var sync: CustomAnimatorSync = CustomAnimatorSyncImpl()

        fun createShowBee(): AnimatorSet {
            val animatorSet = AnimatorSet()
            val alphaShowAnimation =
                createAlphaShowAnimation(
                    binding.helpShowInstructionImageView,
                    0L,
                    DURATION_ANIMATOR_SHOW_BEE
                )
            val scaleAnimation =
                createScaleAnimation(binding.helpShowInstructionImageView, 1f, 0f).apply {
                    duration = DURATION_ANIMATOR_SCALE_BEE
                }
            animatorSet.interpolator = DecelerateInterpolator()
            animatorSet.playSequentially(alphaShowAnimation, scaleAnimation)
            return animatorSet
        }

        fun createSwayTable(): ObjectAnimator {
            return createSwayAnimation(
                binding.instructionsImageView,
                DURATION_ANIMATOR_SHOW_INSTRUCTION,
                ANGLE_SWAY_INSTRUCTION
            )
        }

        fun changeSelectedCard(listCards: List<TypeCards>) {
            TypeCards.values().forEach { typeCard ->
                if (typeCard in listCards) {
                    if (selectCard[typeCard] == null) {
                        val customAnimatorSoaring = CustomAnimatorSoaring(viewCard(typeCard))
                        selectCard[typeCard] = customAnimatorSoaring
                        sync.add(customAnimatorSoaring)
                    }
                    selectCard[typeCard]?.enable = true
                } else {
                    selectCard[typeCard]?.enable = false
                }
            }

        }

        fun startSwayTable() {
            swayTable = createSwayTable().apply { start() }
        }

        fun startShowBee() {
            if (showBee?.isRunning == true) return
            showBee = createShowBee().apply { start() }
        }

        fun end() {
            sync.clear()
            selectCard.forEach { it.value?.end() }
            selectCard.clear()
            showBee?.end()
            swayTable?.end()
        }
    }

    companion object {
        const val TAG_MAIN_MENU_FRAGMENT = "MainMenuFragment"
        const val KEY_RESULT_FROM_REMOVE_PLAYER_DIALOG_FRAGMENT =
            "KeyResultFromRemovePlayerDialogFragment"

        private const val DURATION_ANIMATOR_SHOW_BEE:Long = Conf.DURATION_ANIMATOR_SHOW_BEE
        private const val DURATION_ANIMATOR_SCALE_BEE:Long = Conf.DURATION_ANIMATOR_SCALE_BEE
        private const val DURATION_ANIMATOR_SHOW_INSTRUCTION:Long = Conf.DURATION_ANIMATOR_SHOW_INSTRUCTION
        private const val ANGLE_SWAY_INSTRUCTION: Float = Conf.ANGLE_SWAY_INSTRUCTION

        @JvmStatic
        fun newInstance() = MainMenuFragment()
    }
}