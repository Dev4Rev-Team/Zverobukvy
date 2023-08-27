package ru.gb.zverobukvy.presentation.animal_letters_game

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.App
import ru.gb.zverobukvy.databinding.FragmentAnimalLettersGameBinding
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.use_case.AnimalLettersGameInteractorImpl
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.IsEndGameDialogFragment
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog.DataGameIsOverDialog
import ru.gb.zverobukvy.presentation.animal_letters_game.dialog.game_is_over_dialog.GameIsOverDialogFragment
import ru.gb.zverobukvy.presentation.customview.AssetsImageCash
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomLetterView
import ru.gb.zverobukvy.presentation.customview.CustomWordView
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.enableClickAnimation
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf
import kotlin.math.ceil

class AnimalLettersGameFragment :
    ViewBindingFragment<FragmentAnimalLettersGameBinding>(FragmentAnimalLettersGameBinding::inflate) {
    private var gameStart: GameStart? = null
    private val assertsImageCash: AssetsImageCash by lazy {
        (requireContext().applicationContext as App).assetsImageCash
    }

    private val viewModel: AnimalLettersGameViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {

            val animalLettersCardsRepository =
                (requireContext().applicationContext as App).animalLettersCardsRepository
            val game = AnimalLettersGameInteractorImpl(
                animalLettersCardsRepository, gameStart!!.typesCards, gameStart!!.players
            )
            AnimalLettersGameViewModelImpl(game)
        })[AnimalLettersGameViewModelImpl::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameStart = it.parcelable(GAME_START)
        }
        gameStart ?: throw IllegalArgumentException("not arg gameStart")
        viewModel.onActiveGame()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.ChangingState.CorrectLetter -> {
                    setPositionLetterInWord(it.positionLetterInWord)
                    binding.table.setCorrectLetterCard()
                }

                is AnimalLettersGameState.ChangingState.GuessedWord -> {
                    setPositionLetterInWord(it.positionLetterInWord)
                    if (it.hasNextWord) {
                        requestNextWord()
                    }
                }

                is AnimalLettersGameState.ChangingState.InvalidLetter -> {
                    requestNextPlayer(it)
                }

                is AnimalLettersGameState.ChangingState.NextGuessWord -> {
                    setPictureOfWord(it.wordCard.faceImageName)
                    setWord(it.wordCard)
                    closeTable()

                }

                is AnimalLettersGameState.ChangingState.CloseInvalidLetter -> {
                    closeInvalidCard()
                }

                is AnimalLettersGameState.ChangingState.NextPlayer -> {
                    setPlayer(it.nextWalkingPlayer.name)
                }
            }
        }

        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersGameState.EntireState.EndGameState -> {
                    val players = DataGameIsOverDialog.map(it.players)
                    val data = DataGameIsOverDialog(players, it.gameTime)
                    GameIsOverDialogFragment.instance(data)
                        .show(parentFragmentManager, GameIsOverDialogFragment.TAG)
                }

                is AnimalLettersGameState.EntireState.IsEndGameState -> {
                    IsEndGameDialogFragment.instance()
                        .show(parentFragmentManager, IsEndGameDialogFragment.TAG)
                }

                is AnimalLettersGameState.EntireState.StartGameState -> {
                    setPlayer(it.nextWalkingPlayer.name)
                    initPictureWord(it)
                    setWord(it.wordCard)
                    initTable(it)
                    binding.root.visibility = View.VISIBLE
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

    private fun closeInvalidCard() {
        binding.table.nextPlayer()
    }

    private fun closeTable() {
        binding.table.nextWord()
    }

    private fun initView() {
        binding.nextWord.setOnClickListener {
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

    private fun setPlayer(name: String) {
        binding.playerNameTextView.text = name
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

    private fun requestNextPlayer(state: AnimalLettersGameState.ChangingState.InvalidLetter) {
        binding.nextPlayer.let { button ->
            button.setOnClickListener {
                button.visibility = View.INVISIBLE
                binding.table.setInvalidLetterCard(state.invalidLetterCard)
                viewModel.onClickNextWalkingPlayer()
            }
            button.visibility = View.VISIBLE
        }
    }

    private fun requestNextWord() {
        binding.nextWord.visibility = View.VISIBLE
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
                viewModel.onClickLetterCard(pos)
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

    private fun initPictureWord(startGameState: AnimalLettersGameState.EntireState.StartGameState) {
        binding.wordCustomCard.apply {
            setImageOpenBackground(assertsImageCash.getImage("FACE.webp"))
        }
        setPictureOfWord(startGameState.wordCard.faceImageName)
    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<PlayerInGame>) :
        Parcelable

    companion object {
        const val GAME_START = "GAME_START"

        @JvmStatic
        fun newInstance(gameStart: GameStart) = AnimalLettersGameFragment().apply {
            arguments = Bundle().apply {
                putParcelable(GAME_START, gameStart)
            }
        }
    }
}
