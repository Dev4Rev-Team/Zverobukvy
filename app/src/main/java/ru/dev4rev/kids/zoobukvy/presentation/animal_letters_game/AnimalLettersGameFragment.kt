package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.animalLettersGameSubcomponentContainer
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.configuration.Conf
import ru.dev4rev.kids.zoobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.dev4rev.kids.zoobukvy.databinding.FragmentAnimalLettersGameBinding
import ru.dev4rev.kids.zoobukvy.domain.entity.card.LetterCard
import ru.dev4rev.kids.zoobukvy.domain.entity.card.TypeCards
import ru.dev4rev.kids.zoobukvy.domain.entity.card.WordCard
import ru.dev4rev.kids.zoobukvy.domain.entity.player.Player
import ru.dev4rev.kids.zoobukvy.domain.entity.player.PlayerInGame
import ru.dev4rev.kids.zoobukvy.domain.entity.sound.VoiceActingStatus
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.dialog.IsEndGameDialogFragment
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog.DataGameIsOverDialog
import ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.game_is_over_dialog.GameIsOverDialogFragment
import ru.dev4rev.kids.zoobukvy.presentation.customview.AssetsImageCash
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomCard
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomCardTable
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomLetterView
import ru.dev4rev.kids.zoobukvy.presentation.customview.CustomWordView
import ru.dev4rev.kids.zoobukvy.presentation.customview.createAlphaShowAnimation
import ru.dev4rev.kids.zoobukvy.presentation.customview.createInSideAnimation
import ru.dev4rev.kids.zoobukvy.presentation.sound.SoundEffectPlayer
import ru.dev4rev.kids.zoobukvy.presentation.sound.SoundEnum
import ru.dev4rev.kids.zoobukvy.utility.parcelable
import ru.dev4rev.kids.zoobukvy.utility.ui.ViewBindingFragment
import ru.dev4rev.kids.zoobukvy.utility.ui.enableClickAnimation
import ru.dev4rev.kids.zoobukvy.utility.ui.viewModelProviderFactoryOf
import timber.log.Timber
import kotlin.math.ceil

class AnimalLettersGameFragment :
    ViewBindingFragment<FragmentAnimalLettersGameBinding>(FragmentAnimalLettersGameBinding::inflate) {
    private var gameStart: GameStart? = null
    private lateinit var assertsImageCash: AssetsImageCash
    private lateinit var soundEffectPlayer: SoundEffectPlayer
    private lateinit var imageAvatarLoader: ImageAvatarLoader
    private lateinit var viewModel: AnimalLettersGameViewModel
    private val game = GameProcessingState()
    private val event = GameEvent()
    private lateinit var sound: Sound
    private var wordCardSoundName: String? = null

    private var isEnableClick = true

    private val computer = object {
        var isWalking = false
    }
    var lastStateScreen = StateScreen.NextPlayer

    private val snackbar by lazy {
        Snackbar.make(binding.root, "", Snackbar.LENGTH_INDEFINITE).apply {
            setAction(getString(R.string.ok)) { dismiss() }
        }
    }

    private fun initDagger() {
        imageAvatarLoader = requireContext().appComponent.imageAvatarLoader
        requireContext().animalLettersGameSubcomponentContainer.createAnimalLettersGameSubcomponent(
            gameStart!!.typesCards, gameStart!!.players
        ).also { fragmentComponent ->
            viewModel = ViewModelProvider(
                this,
                viewModelProviderFactoryOf { fragmentComponent.viewModel })[AnimalLettersGameViewModelImpl::class.java]

            assertsImageCash = fragmentComponent.assetsImageCash
            soundEffectPlayer = fragmentComponent.soundEffectPlayer
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameStart = it.parcelable(GAME_START)
        }
        gameStart ?: throw IllegalArgumentException("not arg gameStart")
        initDagger()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animator.createAnimators()
        initSound()
        initChangingStateEventVM()
        intiGameStateEventVM()
        initSystemEventVM()
        initView()
    }

    private fun initSound() {
        sound = Sound(soundEffectPlayer)
    }

    private fun initSystemEventVM() {
        viewModel.getSoundStatusLiveData().observe(viewLifecycleOwner) {
            sound.setEnable(it)
            val icSoundToggle = if (it) R.drawable.ic_sound_on else R.drawable.ic_sound_off
            binding.soundButtonImageView.setImageResource(icSoundToggle)
            binding.lettersSoundButtonLayout.alpha = if (it) VISIBLE_ALPHA else DIMNESS_ALPHA
        }

        viewModel.getVoiceActingStatusLiveData().observe(viewLifecycleOwner) {
            val icSoundLetterToggle = when (it.first) {
                VoiceActingStatus.SOUND -> R.drawable.ic_sound_letter_sound
                VoiceActingStatus.LETTER -> R.drawable.ic_sound_letter_on
                VoiceActingStatus.OFF -> R.drawable.ic_sound_letter_off
            }
            binding.lettersSoundButtonImageView.setImageResource(icSoundLetterToggle)
            sound.setVoiceActingStatus(it.first)
            if (it.second) {
                snackbar.setText(it.first.messageId).show()
            }
        }
    }

    private fun initChangingStateEventVM() {
        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.ChangingState.CorrectLetter -> {
                    Timber.d("ChangingState.CorrectLetter")
                    game.changingStateCorrectLetter(it)
                }

                is AnimalLettersGameState.ChangingState.InvalidLetter -> {
                    Timber.d("ChangingState.InvalidLetter")
                    game.changingStateInvalidLetter(it)
                }

                is AnimalLettersGameState.ChangingState.CloseInvalidLetter -> {
                    Timber.d("ChangingState.CloseInvalidLetter")
                    game.changingStateCloseInvalidLetter(it)
                }

                is AnimalLettersGameState.ChangingState.GuessedWord -> {
                    Timber.d("ChangingState.GuessedWord")
                    game.changingStateGuessedWord(it)
                }

                is AnimalLettersGameState.ChangingState.NextGuessWord -> {
                    Timber.d("ChangingState.NextGuessWord")
                    game.changingStateNextGuessWord(it)
                }

                is AnimalLettersGameState.ChangingState.NextPlayer -> {
                    Timber.d("ChangingState.NextPlayer")
                    game.changingStateNextPlayer(it)
                }

                is AnimalLettersGameState.ChangingState.UpdateLettersCards -> {
                    Timber.d("ChangingState.UpdateLettersCards")
                    game.changingStateUpdateLettersCards(it)
                }
            }
        }
    }

    private fun intiGameStateEventVM() {
        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.EntireState.StartGameState -> {
                    Timber.d("EntireState.StartGameState")
                    game.changingStateStartGameState(it)
                }

                is AnimalLettersGameState.EntireState.IsEndGameState -> {
                    Timber.d("EntireState.IsEndGameState")
                    game.changingStateIsEndGameState()
                }

                is AnimalLettersGameState.EntireState.EndGameState -> {
                    Timber.d("EntireState.EndGameState")
                    game.changingStateEndGameState(it)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        game.startNewGame()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        animator.end()
        super.onDestroyView()
    }

    private fun initView() {
        binding.nextWord.root.setOnClickListener {
            isClick {
                binding.nextWord.root.isClickable = false
                event.onClickNextWord()
            }
        }

        IsEndGameDialogFragment.setOnListenerYes(this) {
            event.onEndGameByUser()
        }

        IsEndGameDialogFragment.setOnListenerNo(this) {
            event.onLoadGame()
        }

        binding.backToMenuButtonLayout.setOnClickListener {
            isClick {
                event.onBackPressed()
            }
        }

        binding.wordCustomCard.setOnClickCardListener(0) {
            isClick {
                event.onClickImageWord()
            }
        }

        binding.wordView.setOnClickListener {
            isClick {
                event.onClickWordView()
            }
        }

        binding.soundButtonLayout.setOnClickListener {
            event.onClickSoundButton()
        }

        binding.lettersSoundButtonLayout.setOnClickListener {
            event.onClickLettersSoundButton()
        }

        binding.cardLevelView.setCards(gameStart!!.typesCards)

        isEnableClick = true

        binding.root.setOnClickListener { hideSnackbar() }
    }

    private fun setPositionLetterInWord(pos: Int) {
        binding.wordView.setPositionLetterInWord(pos)
    }

    private fun setPlayer(player: Player) {
        changePlayerName(player)
        if (player !is Player.ComputerPlayer) {
            binding.table.setWorkClick(true)
            computer.isWalking = false
        } else {
            computer.isWalking = true
        }
    }

    private fun changePlayerName(player: Player) {
        if (binding.playerNameTextView.text != player.name) {
            animator.startChangePlayer(player)
        }
    }


    private fun setPictureOfWord(urlPicture: String) {
        val image = assertsImageCash.getImage(urlPicture)
        binding.wordCustomCard.setImageOpen(image)
        binding.wordCustomCard.setImageCloseBackground(image)
    }

    private fun setWord(wordCard: CustomWordView.WordCardUI) {
        wordCardSoundName = wordCard.soundName
        binding.wordView.setWord(wordCard) {
            CustomLetterView(requireContext())
        }
        delayAndRun(DELAY_SOUND_WORD) { wordCardSoundName?.let { sound.playWord(it) } }
    }

    private fun showScreenNextPlayer() {
        animator.showScreenNextPlayer.start()
        lastStateScreen = StateScreen.NextPlayer
    }

    private fun hideScreenNextPlayer() {
        if (lastStateScreen == StateScreen.NextPlayer) {
            animator.hideScreenNextPlayer.start()
        } else {
            binding.nextPlayer.root.visibility = View.INVISIBLE
        }
    }

    private fun showScreenNextWord() {
        animator.showScreenNextWord.start()
        lastStateScreen = StateScreen.NextWord
    }

    private fun hideScreenNextWord() {
        if (lastStateScreen == StateScreen.NextWord) {
            animator.hideScreenNextWord.start()
        } else {
            binding.nextWord.root.visibility = View.INVISIBLE
        }
    }

    private fun showScreenWalkComputer() {
        animator.showScreenWalkComputer.start()
    }

    private fun hideScreenWalkComputer() {
        animator.hideScreenWalkComputer.start()
    }


    override fun onBackPressed(): Boolean {
        event.onBackPressed()
        return false
    }

    private fun initTable(startGameState: AnimalLettersGameState.EntireState.StartGameState) {
        binding.table.apply {
            setListItem(startGameState.lettersCards, assertsImageCash) {
                CustomCard(requireContext()).apply {
                    enableClickAnimation()
                    elevation = 0f
                    setOnClickCorrectCard { pos -> event.onClickCorrectLetter(pos) }
                }
            }
            setOnClickListener { pos ->
                isClick {
                    setWorkClick(false)
                    event.onClickLetterCard(pos)
                }
            }
            setRatioForTable(
                countCardHorizontally,
                startGameState.lettersCards.size,
                layoutParams as ConstraintLayout.LayoutParams
            )
            setColorCard(startGameState.lettersCards)
        }
    }

    private fun setRatioForTable(
        width: Int,
        countCards: Int,
        layoutParams: ConstraintLayout.LayoutParams,
    ) {
        val height = ceil(countCards / width.toFloat()).toInt()
        layoutParams.dimensionRatio = "$width:$height"
    }

    private fun initPictureWord(picture: String) {
        setPictureOfWord(picture)
    }

    private fun delayAndRun(time: Long, block: () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                delay(time)
            }
            withContext(Dispatchers.Main) {
                block.invoke()
            }
        }
    }

    private fun isClick(isHideSnackbar: Boolean = true, block: () -> Unit) {
        if (isEnableClick) {
            isEnableClick = false
            if (isHideSnackbar && hideSnackbar()) {
                isEnableClick = true
                return
            }

            delayAndRun(DELAY_NEXT_CLICK) { isEnableClick = true }
            block.invoke()
        }
    }

    private fun hideSnackbar(): Boolean {
        if (snackbar.isShown) {
            snackbar.dismiss()
            return true
        }
        return false
    }

    private class Sound(val soundEffectPlayer: SoundEffectPlayer) {

        private var voiceActingStatus: VoiceActingStatus = VoiceActingStatus.OFF
        private val letterCardList = mutableListOf<CustomCardTable.LetterCardUI>()

        fun setVoiceActingStatus(voiceActingStatus: VoiceActingStatus) {
            this.voiceActingStatus = voiceActingStatus
        }

        fun initLettersCards(lettersCards: List<CustomCardTable.LetterCardUI>) {
            letterCardList.clear()
            letterCardList.addAll(lettersCards)
        }

        fun playLetter(pos: Int) {
            val letterCard = letterCardList[pos]
            playLetter(letterCard)
        }

        fun playLetter(letterCard: CustomCardTable.LetterCardUI) {
            if (voiceActingStatus == VoiceActingStatus.OFF) return
            soundEffectPlayer.play(letterCard.soundName)
        }


        fun setEnable(it: Boolean) {
            soundEffectPlayer.setEnable(it)
        }

        fun playWord(wordCardSoundName: String) {
            soundEffectPlayer.play(wordCardSoundName)
        }

        fun playEffect(soundEnum: SoundEnum) {
            soundEffectPlayer.play(soundEnum)
        }

    }

    private inner class GameProcessingState {
        fun startNewGame() {
            viewModel.onActiveGame()
        }

        fun changingStateCorrectLetter(it: AnimalLettersGameState.ChangingState.CorrectLetter) {
            soundFlipLetter(SoundEnum.CARD_IS_SUCCESSFUL, it.correctLetterCard)
            setPositionLetterInWord(it.positionLetterInWord)
            binding.table.openCard(it.correctLetterCard)
            binding.table.setCorrectlyCard(it.correctLetterCard)
            delayAndRun(DELAY_NEXT_CLICK) { binding.table.setWorkClick(true) }
        }

        fun changingStateInvalidLetter(it: AnimalLettersGameState.ChangingState.InvalidLetter) {
            soundFlipLetter(SoundEnum.CARD_IS_UNSUCCESSFUL, it.invalidLetterCard)
            binding.table.openCard(it.invalidLetterCard)
            showScreenNextPlayer()
            if (computer.isWalking) {
                hideScreenWalkComputer()
            }
        }

        fun changingStateCloseInvalidLetter(it: AnimalLettersGameState.ChangingState.CloseInvalidLetter) {
            binding.table.closeCard(it.invalidLetterCard)
        }

        fun changingStateGuessedWord(it: AnimalLettersGameState.ChangingState.GuessedWord) {
            soundFlipLetter(SoundEnum.WORD_IS_GUESSED, it.correctLetterCard)
            setPositionLetterInWord(it.positionLetterInWord)
            binding.table.openCard(it.correctLetterCard)
            if (it.hasNextWord) {
                showScreenNextWord()
            }
            if (computer.isWalking) {
                hideScreenWalkComputer()
            }
        }

        fun changingStateNextGuessWord(it: AnimalLettersGameState.ChangingState.NextGuessWord) {
            setPictureOfWord(it.wordCard.faceImageName)
            setWord(it.wordCard)
            binding.table.closeCardAll()
            setNumberInGameCards(it.wordCard)
            delayAndRun(Conf.DURATION_FLIP) { binding.table.setColorCard(it.updatedLettersCards) }
            sound.initLettersCards(it.updatedLettersCards)
        }

        fun changingStateNextPlayer(it: AnimalLettersGameState.ChangingState.NextPlayer) {
            setPlayer(it.nextWalkingPlayer.player)
            when (lastStateScreen) {
                StateScreen.NextPlayer -> hideScreenNextPlayer()
                StateScreen.NextWord -> hideScreenNextWord()
            }
            if (computer.isWalking) {
                showScreenWalkComputer()
            }
        }

        fun changingStateStartGameState(it: AnimalLettersGameState.EntireState.StartGameState) {
            setPlayer(it.nextWalkingPlayer.player)
            initPictureWord(it.wordCard.faceImageName)
            setWord(it.wordCard)
            initTable(it)
            sound.initLettersCards(it.lettersCards)
            binding.root.visibility = View.VISIBLE
            if (it.nextPlayerBtnVisible) {
                showScreenNextPlayer()
            } else if (it.nextWordBtnVisible) {
                showScreenNextWord()
            } else {
                binding.table.setWorkClick(true)
            }
            setNumberInGameCards(it.wordCard)
        }

        fun changingStateIsEndGameState() {
            IsEndGameDialogFragment.instance()
                .show(parentFragmentManager, IsEndGameDialogFragment.TAG)
        }

        fun changingStateEndGameState(it: AnimalLettersGameState.EntireState.EndGameState) {
            if (it.isFastEndGame && !Conf.DEBUG_IS_SHOW_GAME_IS_OVER_DIALOG_ANYTIME) {
                event.popBackStack()
                requireContext().animalLettersGameSubcomponentContainer.deleteAnimalLettersGameSubcomponent()
            } else {
                val players = DataGameIsOverDialog.map(it.players)
                val data = DataGameIsOverDialog(players, it.gameTime, it.bestTime, it.isRecordTime)
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    GameIsOverDialogFragment.instance(data),
                    GameIsOverDialogFragment.TAG
                ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss()
                sound.playEffect(SoundEnum.GAME_OVER)
            }
        }

        fun changingStateUpdateLettersCards(it: AnimalLettersGameState.ChangingState.UpdateLettersCards) {
            binding.table.setColorCard(it.updatedLettersCards)
            sound.initLettersCards(it.updatedLettersCards)
        }
    }

    private fun setNumberInGameCards(wordCard: WordCard) {
        binding.numberInGameCardsTextView.text =
            getString(
                R.string.number_in_game_cards,
                wordCard.numberInGame.individualNumber,
                wordCard.numberInGame.totalNumber
            )
    }

    private fun soundFlipLetter(
        effectSoundEnum: SoundEnum, letterCard: LetterCard,
    ) {
        sound.playEffect(SoundEnum.CARD_IS_FLIP)
        delayAndRun(DELAY_SOUND_EFFECT) { sound.playEffect(effectSoundEnum) }
        delayAndRun(DELAY_SOUND_LETTER) { sound.playLetter(letterCard) }
    }


    private inner class GameEvent {
        fun onEndGameByUser() {
            viewModel.onEndGameByUser()
        }

        fun onLoadGame() {
            viewModel.onLoadGame()
        }

        fun onBackPressed() {
            viewModel.onBackPressed()
        }

        fun onClickNextWord() {
            //todo viewModel.onClickNextWord()
        }

        fun popBackStack() {
            parentFragmentManager.popBackStack()
        }

        fun onClickLetterCard(pos: Int) {
            viewModel.onClickLetterCard(pos)
        }

        fun onClickImageWord() {
            onClickWordView()
        }

        fun onClickWordView() {
            delayAndRun(DELAY_SOUND_REPEAT) { wordCardSoundName?.let { sound.playWord(it) } }
        }

        fun onClickCorrectLetter(position: Int) {
            delayAndRun(DELAY_SOUND_REPEAT) {
                sound.playLetter(position)
            }
        }

        fun onClickSoundButton() {
            isClick {
                viewModel.onSoundClick()
            }
        }

        fun onClickLettersSoundButton() {
            isClick(false) {
                viewModel.onVoiceActingClick()
            }
        }

    }

    private val animator = object {
        lateinit var showScreenNextPlayer: ObjectAnimator
        lateinit var showScreenNextWord: ObjectAnimator
        lateinit var showScreenWalkComputer: ObjectAnimator
        lateinit var hideScreenNextPlayer: ObjectAnimator
        lateinit var hideScreenNextWord: ObjectAnimator
        lateinit var hideScreenWalkComputer: ObjectAnimator
        private lateinit var changePlayer: AnimatorSet
        private lateinit var playerForChange: Player
        fun createAnimators() {
            showScreenNextPlayer = createAlphaShowAnimation(
                binding.nextPlayer.root,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING
            )
            showScreenNextWord = createAlphaShowAnimation(
                binding.nextWord.root,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING
            )
            showScreenWalkComputer = createAlphaShowAnimation(
                binding.walkComputer.root,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING,
            )
            hideScreenNextPlayer = createAlphaShowAnimation(
                binding.nextPlayer.root,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING,
                false
            ).apply {
                doOnEnd {
                    binding.nextPlayer.root.visibility = View.INVISIBLE
                }
            }
            hideScreenNextWord = createAlphaShowAnimation(
                binding.nextWord.root,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING,
                false
            ).apply {
                doOnEnd {
                    binding.nextWord.root.visibility = View.INVISIBLE
                    binding.nextWord.root.isClickable = true
                }
            }
            hideScreenWalkComputer =
                createAlphaShowAnimation(
                    binding.walkComputer.root,
                    START_DELAY_ANIMATION_SCREEN_DIMMING,
                    DURATION_ANIMATION_SCREEN_DIMMING,
                    false
                ).apply {
                    doOnEnd { binding.walkComputer.root.visibility = View.INVISIBLE }
                }
            changePlayer = createInSideAnimation(
                binding.playerNameCard, DURATION_ANIMATOR_NEXT_PLAYER,
                SHIFT_ANIMATOR_PLAYER_NEXT_DP
            ) {
                binding.playerNameCard.visibility = View.VISIBLE
                binding.playerNameTextView.text = playerForChange.name
                imageAvatarLoader.loadImageAvatar(
                    playerForChange.avatar,
                    binding.playerAvatarImageView
                )
            }

        }

        fun startChangePlayer(player: Player) {
            playerForChange = player
            changePlayer.start()
        }

        fun end() {
            showScreenNextPlayer.end()
            showScreenNextWord.end()
            showScreenWalkComputer.end()
            hideScreenNextPlayer.end()
            hideScreenNextWord.end()
            hideScreenWalkComputer.end()
            changePlayer.end()
        }

    }

    enum class StateScreen {
        NextPlayer,
        NextWord
    }

    @Parcelize
    class GameStart(val typesCards: List<TypeCards>, val players: List<PlayerInGame>) :
        Parcelable

    companion object {
        const val GAME_START = "GAME_START"
        const val TAG_ANIMAL_LETTERS_FRAGMENT = "GameAnimalLettersFragment"

        const val DELAY_NEXT_CLICK = Conf.DELAY_NEXT_CLICK

        private const val START_DELAY_ANIMATION_SCREEN_DIMMING =
            Conf.START_DELAY_ANIMATION_SCREEN_DIMMING
        private const val DURATION_ANIMATION_SCREEN_DIMMING =
            Conf.DURATION_ANIMATION_SCREEN_DIMMING

        private const val DELAY_SOUND_WORD = Conf.DELAY_SOUND_WORD
        private const val DELAY_SOUND_EFFECT = Conf.DELAY_SOUND_EFFECT
        private const val DELAY_SOUND_LETTER = Conf.DELAY_SOUND_LETTER
        private const val DELAY_SOUND_REPEAT = Conf.DELAY_SOUND_REPEAT

        private const val DURATION_ANIMATOR_NEXT_PLAYER = Conf.DURATION_ANIMATOR_NEXT_PLAYER
        private const val SHIFT_ANIMATOR_PLAYER_NEXT_DP = Conf.SHIFT_ANIMATOR_PLAYER_NEXT_DP

        private const val VISIBLE_ALPHA = 1f
        private const val DIMNESS_ALPHA = 0.75f

        @JvmStatic
        fun newInstance(gameStart: GameStart) = AnimalLettersGameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(GAME_START, gameStart)
            }
        }
    }
}
