package ru.gb.zverobukvy.presentation.animal_letters_game

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.databinding.FragmentAnimalLettersGameBinding
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.IsEndGameDialogFragment
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog.DataGameIsOverDialog
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog.GameIsOverDialogFragment
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomLetterView
import ru.gb.zverobukvy.presentation.customview.CustomWordView
import ru.gb.zverobukvy.presentation.customview.createAlphaShowAnimation
import ru.gb.zverobukvy.presentation.sound.SoundEffectPlayer
import ru.gb.zverobukvy.presentation.sound.SoundEnum
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.enableClickAnimation
import ru.gb.zverobukvy.utility.ui.image_avatar_loader.ImageAvatarLoader
import ru.gb.zverobukvy.utility.ui.image_avatar_loader.ImageAvatarLoaderImpl
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf
import kotlin.math.ceil


class AnimalLettersGameFragment :
    ViewBindingFragment<FragmentAnimalLettersGameBinding>(FragmentAnimalLettersGameBinding::inflate) {
    private var gameStart: GameStart? = null

    private lateinit var assertsImageCash: AssetsImageCash

    private lateinit var soundEffectPlayer: SoundEffectPlayer

    private lateinit var viewModel: AnimalLettersGameViewModel

    private var imageAvatarLoader: ImageAvatarLoader = ImageAvatarLoaderImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameStart = it.parcelable(GAME_START)
        }
        gameStart ?: throw IllegalArgumentException("not arg gameStart")

        initDagger()

        viewModel.onActiveGame()
    }

    private fun initDagger() {
        requireContext().appComponent.getAnimalLettersGameSubcomponentFactory().create(
            gameStart!!.typesCards,
            gameStart!!.players
        ).also { fragmentComponent ->
            viewModel = ViewModelProvider(
                this,
                viewModelProviderFactoryOf { fragmentComponent.viewModel }
            )[AnimalLettersGameViewModelImpl::class.java]

            assertsImageCash = fragmentComponent.assetsImageCash
            soundEffectPlayer = fragmentComponent.soundEffectPlayer
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.ChangingState.CorrectLetter -> {
                    soundEffectPlayer.play(SoundEnum.CARD_IS_SUCCESSFUL)
                    setPositionLetterInWord(it.positionLetterInWord)
                    binding.table.openCard(it.correctLetterCard)
                    binding.table.setWorkClick(true)
                }

                is AnimalLettersGameState.ChangingState.GuessedWord -> {
                    soundEffectPlayer.play(SoundEnum.WORD_IS_GUESSED)
                    setPositionLetterInWord(it.positionLetterInWord)
                    binding.table.openCard(it.correctLetterCard)
                    if (it.hasNextWord) {
                        requestNextWord(it.screenDimmingText)
                    }
                }

                is AnimalLettersGameState.ChangingState.InvalidLetter -> {
                    soundEffectPlayer.play(SoundEnum.CARD_IS_UNSUCCESSFUL)
                    binding.table.openCard(it.invalidLetterCard)
                    requestNextPlayer(it.screenDimmingText)
                }

                is AnimalLettersGameState.ChangingState.NextGuessWord -> {
                    setPictureOfWord(it.wordCard.faceImageName)
                    setWord(it.wordCard)
                    binding.table.closeCardAll()
                }

                is AnimalLettersGameState.ChangingState.CloseInvalidLetter -> {
                    binding.table.closeCard(it.invalidLetterCard)
                }

                is AnimalLettersGameState.ChangingState.NextPlayer -> {
                    setPlayer(it.nextWalkingPlayer.player)
                }
            }
        }

        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.EntireState.EndGameState -> {
                    if (it.isFastEndGame) {
                        parentFragmentManager.popBackStack()
                    } else {
                        val players = DataGameIsOverDialog.map(it.players)
                        val data = DataGameIsOverDialog(players, it.gameTime)
                        soundEffectPlayer.play(SoundEnum.GAME_OVER)
                        GameIsOverDialogFragment.instance(data)
                            .show(parentFragmentManager, GameIsOverDialogFragment.TAG)
                    }

                }

                is AnimalLettersGameState.EntireState.IsEndGameState -> {
                    IsEndGameDialogFragment.instance()
                        .show(parentFragmentManager, IsEndGameDialogFragment.TAG)
                }

                is AnimalLettersGameState.EntireState.StartGameState -> {
                    setPlayer(it.nextWalkingPlayer.player)
                    initPictureWord(it.wordCard.faceImageName)
                    setWord(it.wordCard)
                    initTable(it)
                    binding.root.visibility = View.VISIBLE

                    if (it.nextPlayerBtnVisible) {
                        requestNextPlayer(it.screenDimmingText)
                    } else if (it.nextWordBtnVisible) {
                        requestNextWord(it.screenDimmingText)
                    } else {
                        binding.table.setWorkClick(true)
                    }
                }
            }
        }

        initView()
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
            viewModel.onClickNextWord()
        }

        GameIsOverDialogFragment.setOnListenerClose(this) {
            parentFragmentManager.popBackStack()
        }

        IsEndGameDialogFragment.setOnListenerYes(this) {
            viewModel.onEndGameByUser()
        }

        IsEndGameDialogFragment.setOnListenerNo(this) {
            viewModel.onLoadGame()
        }

        binding.backToMenuImageButton.setOnClickListener {
            viewModel.onBackPressed()
        }

        binding.cardLevel.setCards(gameStart!!.typesCards)
    }

    private fun setPositionLetterInWord(pos: Int) {
        binding.wordView.setPositionLetterInWord(pos)
    }

    private fun setPlayer(player: Player) {
        binding.playerNameTextView.text = player.name
        imageAvatarLoader.loadImageAvatar(player.avatar, binding.playerAvatarImageView)
        binding.table.setWorkClick(true)
    }

    private fun setPictureOfWord(urlPicture: String) {
        val image = assertsImageCash.getImage(urlPicture)
        binding.wordCustomCard.setImageSide(
            image, image
        )
    }

    private fun setWord(wordCard: CustomWordView.WordCardUI) {
        binding.wordView.setWord(wordCard) {
            CustomLetterView(requireContext())
        }
    }

    private fun requestNextPlayer(screenDimmingText: String) {
        binding.nextPlayer.root.let { button ->
            button.setOnClickListener {
                button.visibility = View.INVISIBLE
                viewModel.onClickNextWalkingPlayer()
            }
            createAlphaShowAnimation(button, START_DELAY_ANIMATION, DURATION_ANIMATION).start()
        }
        binding.nextPlayer.nextPlayerTextView.text = screenDimmingText
    }

    private fun requestNextWord(screenDimmingText: String) {
        createAlphaShowAnimation(
            binding.nextWord.root,
            START_DELAY_ANIMATION,
            DURATION_ANIMATION
        ).start()
        binding.nextWord.nextWordMoveTextView.text = screenDimmingText
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return false
    }

    private fun initTable(startGameState: AnimalLettersGameState.EntireState.StartGameState) {
        binding.table.apply {
            setListItem(startGameState.lettersCards, assertsImageCash) {
                CustomCard(requireContext()).apply {
                    enableClickAnimation()
                    setImageOpenBackground(assertsImageCash.getImage("FACE.webp"))
                }
            }
            setOnClickListener { pos ->
                setWorkClick(false)
                viewModel.onClickLetterCard(pos)
                soundEffectPlayer.play(SoundEnum.CARD_IS_FLIP)
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
            setImageOpenBackground(assertsImageCash.getImage("FACE.webp"))
        }
        setPictureOfWord(picture)
    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<PlayerInGame>) :
        Parcelable

    companion object {
        const val GAME_START = "GAME_START"
        const val TAG_ANIMAL_LETTERS_FRAGMENT = "GameAnimalLettersFragment"

        private const val START_DELAY_ANIMATION = 350L
        private const val DURATION_ANIMATION = 100L

        @JvmStatic
        fun newInstance(gameStart: GameStart) = AnimalLettersGameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(GAME_START, gameStart)
            }
        }
    }
}
