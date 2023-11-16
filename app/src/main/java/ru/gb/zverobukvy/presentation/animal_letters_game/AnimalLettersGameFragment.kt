package ru.gb.zverobukvy.presentation.animal_letters_game

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.configuration.Conf
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.data.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.databinding.FragmentAnimalLettersGameBinding
import ru.gb.zverobukvy.domain.entity.player.Player
import ru.gb.zverobukvy.domain.entity.player.PlayerInGame
import ru.gb.zverobukvy.domain.entity.card.TypeCards
import ru.gb.zverobukvy.domain.entity.card.LetterCard
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.IsEndGameDialogFragment
import ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog.DataGameIsOverDialog
import ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog.GameIsOverDialogFragment
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomCardTable
import ru.gb.zverobukvy.presentation.customview.CustomLetterView
import ru.gb.zverobukvy.presentation.customview.CustomWordView
import ru.gb.zverobukvy.presentation.customview.createAlphaShowAnimation
import ru.gb.zverobukvy.presentation.customview.createInSideAnimation
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer
import ru.gb.zverobukvy.presentation.sound.SoundEnum
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.enableClickAnimation
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf
import kotlin.math.ceil

class AnimalLettersGameFragment :
    ViewBindingFragment<FragmentAnimalLettersGameBinding>(FragmentAnimalLettersGameBinding::inflate) {
    private var gameStart: GameStart? = null
    private lateinit var assertsImageCash: AssetsImageCash
    private lateinit var soundEffectPlayer: SoundEffectPlayer
    private lateinit var viewModel: AnimalLettersGameViewModel
    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl
    private val game = GameWork()
    private val event = GameEvent()
    private var wordCardSoundName: String? = null
    private var mapLettersSoundName = mutableMapOf<Int, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameStart = it.parcelable(GAME_START)
        }
        gameStart ?: throw IllegalArgumentException("not arg gameStart")
        initDagger()
        game.startNewGame()
    }

    private fun initDagger() {
        requireContext().appComponent.getAnimalLettersGameSubcomponentFactory().create(
            gameStart!!.typesCards, gameStart!!.players
        ).also { fragmentComponent ->
            viewModel = ViewModelProvider(
                this,
                viewModelProviderFactoryOf { fragmentComponent.viewModel })[AnimalLettersGameViewModelImpl::class.java]

            assertsImageCash = fragmentComponent.assetsImageCash
            soundEffectPlayer = fragmentComponent.soundEffectPlayer
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initChangingStateEvent()
        intiGameStateEvent()
        initSystemEvent()
        initView()
    }

    private fun initSystemEvent() {
        viewModel.getSoundStatusLiveData().observe(viewLifecycleOwner) {
            soundEffectPlayer.setEnable(it)
            binding.soundToggleButton.isChecked = it
        }
    }

    private fun initChangingStateEvent() {
        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.ChangingState.CorrectLetter -> {
                    game.changingStateCorrectLetter(it)
                }

                is AnimalLettersGameState.ChangingState.InvalidLetter -> {
                    game.changingStateInvalidLetter(it)
                }

                is AnimalLettersGameState.ChangingState.CloseInvalidLetter -> {
                    game.changingStateCloseInvalidLetter(it)
                }

                is AnimalLettersGameState.ChangingState.GuessedWord -> {
                    game.changingStateGuessedWord(it)
                }

                is AnimalLettersGameState.ChangingState.NextGuessWord -> {
                    game.changingStateNextGuessWord(it)
                }

                is AnimalLettersGameState.ChangingState.NextPlayer -> {
                    game.changingStateNextPlayer(it)
                }
            }
        }
    }

    private fun intiGameStateEvent() {
        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.EntireState.StartGameState -> {
                    game.changingStateStartGameState(it)
                }

                is AnimalLettersGameState.EntireState.IsEndGameState -> {
                    game.changingStateIsEndGameState()
                }

                is AnimalLettersGameState.EntireState.EndGameState -> {
                    game.changingStateEndGameState(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume()
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }

    private fun initView() {
        binding.nextWord.root.setOnClickListener {
            it.visibility = View.INVISIBLE
            event.onClickNextWord()
        }

        IsEndGameDialogFragment.setOnListenerYes(this) {
            event.onEndGameByUser()
        }

        IsEndGameDialogFragment.setOnListenerNo(this) {
            event.onLoadGame()
        }

        binding.backToMenuImageButton.setOnClickListener {
            event.onBackPressed()
        }

        binding.wordCustomCard.setOnClickCardListener(0) {
            event.onClickImageWord()
        }

        binding.wordView.setOnClickListener {
            event.onClickWordView()
        }

        binding.soundToggleButton.setOnClickListener {
            viewModel.onSoundClick()
        }

        binding.cardLevel.setCards(gameStart!!.typesCards)
    }

    private fun setPositionLetterInWord(pos: Int) {
        binding.wordView.setPositionLetterInWord(pos)
    }

    private fun setPlayer(player: Player) {
        if (binding.playerNameTextView.text != player.name) {
            createInSideAnimation(
                binding.playerNameCard, DURATION_ANIMATOR_NEXT_PLAYER,
                SHIFT_ANIMATOR_PLAYER_NEXT_DP
            ) { _ ->
                binding.playerNameCard.visibility = View.VISIBLE
                binding.playerNameTextView.text = player.name
                imageAvatarLoader.loadImageAvatar(player.avatar, binding.playerAvatarImageView)
            }.start()
        }
        if (player !is Player.ComputerPlayer) {
            binding.table.setWorkClick(true)
        }
    }

    private fun setPictureOfWord(urlPicture: String) {
        val image = assertsImageCash.getImage(urlPicture)
        binding.wordCustomCard.setImageSide(
            image, image
        )
    }

    private fun setWord(wordCard: CustomWordView.WordCardUI) {
        wordCardSoundName = wordCard.soundName
        binding.wordView.setWord(wordCard) {
            CustomLetterView(requireContext())
        }
        delayAndRun(DELAY_SOUND_WORD) { soundEffectPlayer.play(wordCard.soundName) }
    }

    private fun requestNextPlayer(screenDimmingText: String) {
        binding.nextPlayer.root.let { button ->
            button.setOnClickListener {
                button.visibility = View.INVISIBLE
                event.onClickNextWalkingPlayer()
            }
            createAlphaShowAnimation(
                button,
                START_DELAY_ANIMATION_SCREEN_DIMMING,
                DURATION_ANIMATION_SCREEN_DIMMING
            ).start()
        }
        binding.nextPlayer.nextPlayerTextView.text = screenDimmingText
    }

    private fun requestNextWord(screenDimmingText: String) {
        createAlphaShowAnimation(
            binding.nextWord.root,
            START_DELAY_ANIMATION_SCREEN_DIMMING,
            DURATION_ANIMATION_SCREEN_DIMMING
        ).start()
        binding.nextWord.nextWordMoveTextView.text = screenDimmingText
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
                    setImageOpenBackground(assertsImageCash.getImage(IMAGE_CARD_FOREGROUND))
                    setOnClickCorrectCard { pos -> event.onClickCorrectLetter(pos) }
                }
            }
            setOnClickListener { pos ->
                setWorkClick(false)
                event.onClickLetterCard(pos)
            }
            setRatioForTable(
                countCardHorizontally,
                startGameState.lettersCards.size,
                layoutParams as ConstraintLayout.LayoutParams
            )
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
        binding.wordCustomCard.apply {
            setImageOpenBackground(assertsImageCash.getImage(IMAGE_CARD_FOREGROUND))
        }
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

    private fun saveLettersSoundName(lettersCards: List<CustomCardTable.LetterCardUI>) {
        mapLettersSoundName.clear()
        lettersCards.forEachIndexed { index, value ->
            mapLettersSoundName[index] = value.soundName
        }
    }

    private inner class GameWork() {
        fun startNewGame() {
            viewModel.onActiveGame()
        }

        fun changingStateCorrectLetter(it: AnimalLettersGameState.ChangingState.CorrectLetter) {
            soundFlipLetter(SoundEnum.CARD_IS_SUCCESSFUL, it.correctLetterCard)
            setPositionLetterInWord(it.positionLetterInWord)
            binding.table.openCard(it.correctLetterCard)
            binding.table.setCorrectlyCard(it.correctLetterCard)
            delayAndRun(DELAY_ENABLE_CLICK_LETTERS_CARD) { binding.table.setWorkClick(true) }
        }

        fun changingStateInvalidLetter(it: AnimalLettersGameState.ChangingState.InvalidLetter) {
            soundFlipLetter(SoundEnum.CARD_IS_UNSUCCESSFUL, it.invalidLetterCard)
            binding.table.openCard(it.invalidLetterCard)
            requestNextPlayer(it.screenDimmingText)
        }

        fun changingStateCloseInvalidLetter(it: AnimalLettersGameState.ChangingState.CloseInvalidLetter) {
            binding.table.closeCard(it.invalidLetterCard)
        }

        fun changingStateGuessedWord(it: AnimalLettersGameState.ChangingState.GuessedWord) {
            soundFlipLetter(SoundEnum.WORD_IS_GUESSED, it.correctLetterCard)
            setPositionLetterInWord(it.positionLetterInWord)
            binding.table.openCard(it.correctLetterCard)
            if (it.hasNextWord) {
                requestNextWord(it.screenDimmingText)
            }
        }

        fun changingStateNextGuessWord(it: AnimalLettersGameState.ChangingState.NextGuessWord) {
            setPictureOfWord(it.wordCard.faceImageName)
            setWord(it.wordCard)
            binding.table.closeCardAll()
            binding.nextWord.root.let {
                if (it.visibility == View.VISIBLE) it.visibility = View.INVISIBLE
            }
        }

        fun changingStateNextPlayer(it: AnimalLettersGameState.ChangingState.NextPlayer) {
            binding.nextPlayer.root.let {
                if (it.visibility == View.VISIBLE) it.visibility = View.INVISIBLE
            }
            setPlayer(it.nextWalkingPlayer.player)
        }

        fun changingStateStartGameState(it: AnimalLettersGameState.EntireState.StartGameState) {
            setPlayer(it.nextWalkingPlayer.player)
            initPictureWord(it.wordCard.faceImageName)
            setWord(it.wordCard)
            initTable(it)
            saveLettersSoundName(it.lettersCards)
            binding.root.visibility = View.VISIBLE
            if (it.nextPlayerBtnVisible) {
                requestNextPlayer(it.screenDimmingText)
            } else if (it.nextWordBtnVisible) {
                requestNextWord(it.screenDimmingText)
            } else {
                binding.table.setWorkClick(true)
            }
        }

        fun changingStateIsEndGameState() {
            IsEndGameDialogFragment.instance()
                .show(parentFragmentManager, IsEndGameDialogFragment.TAG)
        }

        fun changingStateEndGameState(it: AnimalLettersGameState.EntireState.EndGameState) {
            if (it.isFastEndGame) {
                event.popBackStack()
            } else {
                val players = DataGameIsOverDialog.map(it.players)
                val data = DataGameIsOverDialog(players, it.gameTime)
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.container,
                    GameIsOverDialogFragment.instance(data),
                    GameIsOverDialogFragment.TAG
                ).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commitAllowingStateLoss()
                soundEffectPlayer.play(SoundEnum.GAME_OVER)
            }
        }

    }

    private fun soundFlipLetter(
        effectSoundEnum: SoundEnum, correctLetterCard: LetterCard
    ) {
        soundEffectPlayer.play(SoundEnum.CARD_IS_FLIP)
        delayAndRun(DELAY_SOUND_EFFECT) { soundEffectPlayer.play(effectSoundEnum) }
        delayAndRun(DELAY_SOUND_LETTER) { soundEffectPlayer.play(correctLetterCard.soundName) }
    }

    private inner class GameEvent() {
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
            viewModel.onClickNextWord()
        }

        fun popBackStack() {
            parentFragmentManager.popBackStack()
        }

        fun onClickLetterCard(pos: Int) {
            viewModel.onClickLetterCard(pos)
        }

        fun onClickNextWalkingPlayer() {
            viewModel.onClickNextWalkingPlayer()
        }

        fun onClickImageWord() {
            onClickWordView()
        }

        fun onClickWordView() {
            delayAndRun(DELAY_SOUND_REPEAT) { wordCardSoundName?.let { soundEffectPlayer.play(it) } }
        }

        fun onClickCorrectLetter(position: Int) {
            delayAndRun(DELAY_SOUND_REPEAT) {
                mapLettersSoundName[position]?.let { soundEffectPlayer.play(it) }
            }
        }

    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<PlayerInGame>) :
        Parcelable

    companion object {
        const val GAME_START = "GAME_START"
        const val TAG_ANIMAL_LETTERS_FRAGMENT = "GameAnimalLettersFragment"

        private const val START_DELAY_ANIMATION_SCREEN_DIMMING =
            Conf.START_DELAY_ANIMATION_SCREEN_DIMMING
        private const val DURATION_ANIMATION_SCREEN_DIMMING = Conf.DURATION_ANIMATION_SCREEN_DIMMING

        private const val DELAY_SOUND_WORD = Conf.DELAY_SOUND_WORD
        private const val DELAY_SOUND_EFFECT = Conf.DELAY_SOUND_EFFECT
        private const val DELAY_SOUND_LETTER = Conf.DELAY_SOUND_LETTER
        private const val DELAY_SOUND_REPEAT = Conf.DELAY_SOUND_REPEAT
        private const val DELAY_ENABLE_CLICK_LETTERS_CARD = Conf.DELAY_ENABLE_CLICK_LETTERS_CARD

        private const val IMAGE_CARD_FOREGROUND = Conf.IMAGE_CARD_FOREGROUND

        private const val DURATION_ANIMATOR_NEXT_PLAYER = Conf.DURATION_ANIMATOR_NEXT_PLAYER
        private const val SHIFT_ANIMATOR_PLAYER_NEXT_DP = Conf.SHIFT_ANIMATOR_PLAYER_NEXT_DP


        @JvmStatic
        fun newInstance(gameStart: GameStart) = AnimalLettersGameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(GAME_START, gameStart)
            }
        }
    }
}
