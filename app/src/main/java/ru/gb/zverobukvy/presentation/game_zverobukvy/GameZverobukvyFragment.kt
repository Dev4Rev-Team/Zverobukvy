package ru.gb.zverobukvy.presentation.game_zverobukvy

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.lifecycle.ViewModelProvider
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.data.data_source_impl.LetterCardsDBImpl
import ru.gb.zverobukvy.data.data_source_impl.WordCardsDBImpl
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersCardsRepositoryImpl
import ru.gb.zverobukvy.databinding.FragmentGameZverobukvyBinding
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState
import ru.gb.zverobukvy.domain.entity.PlayerInGame
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractorImpl
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomLetterView
import ru.gb.zverobukvy.presentation.customview.CustomWordView
import ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog.DataGameIsOverDialog
import ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog.GameIsOverDialogFragment
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.enableClickAnimation
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf

class GameZverobukvyFragment :
    ViewBindingFragment<FragmentGameZverobukvyBinding>(FragmentGameZverobukvyBinding::inflate) {
    private var gameStart: GameStart? = null

    private val viewModel: GameZverobukvyViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {

            val letterCardsDB: LetterCardsDB = LetterCardsDBImpl()
            val wordCardsDB: WordCardsDB = WordCardsDBImpl()
            val animalLettersCardsRepository =
                AnimalLettersCardsRepositoryImpl(letterCardsDB, wordCardsDB)
            val game = AnimalLettersInteractorImpl(
                animalLettersCardsRepository,
                gameStart!!.typesCards,
                gameStart!!.players
            )
            GameZverobukvyViewModelImpl(game)
        })[GameZverobukvyViewModelImpl::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            gameStart = it.parcelable(GAME_START)
        }
        gameStart ?: throw IllegalArgumentException("not arg gameStart")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onActiveGame()

        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersState.ChangingState.CorrectLetter -> {
                    setPositionLetterInWord(it.positionLetterInWord)
                    binding.table.setCorrectLetterCard()
                }

                is AnimalLettersState.ChangingState.GuessedWord -> {
                    setPositionLetterInWord(it.positionLetterInWord)
                    if (it.hasNextWord) {
                        requestNextWord()
                    }
                }

                is AnimalLettersState.ChangingState.InvalidLetter -> {
                    requestNextPlayer(it)
                }

                is AnimalLettersState.ChangingState.NextGuessWord -> {
                    setPictureOfWord(it.wordCard.faceImageName)
                    setWord(it.wordCard)
                    closeTable()

                }

                is AnimalLettersState.ChangingState.CloseInvalidLetter -> {
                    closeInvalidCard()
                }

                is AnimalLettersState.ChangingState.NextPlayer -> {
                    setPlayer(it.nextWalkingPlayer.name)
                }
            }
        }

        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersState.EntireState.EndGameState -> {
                    val players = DataGameIsOverDialog.map(it.players)
                    val data = DataGameIsOverDialog(players, it.gameTime)
                    GameIsOverDialogFragment.instance(data)
                        .show(parentFragmentManager, GameIsOverDialogFragment.TAG)
                }

                is AnimalLettersState.EntireState.IsEndGameState -> {
                    IsEndGameDialogFragment.instance()
                        .show(parentFragmentManager, IsEndGameDialogFragment.TAG)
                }

                is AnimalLettersState.EntireState.StartGameState -> {
                    setPlayer(it.nextWalkingPlayer.name)
                    initPictureWord(it)
                    setWord(it.wordCard)
                    initTable(it)
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
        binding.nextWordButton.setOnClickListener {
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
    }

    private fun setPositionLetterInWord(pos: Int) {
        binding.wordView.setPositionLetterInWord(pos)
    }

    private fun setPlayer(name: String) {
        binding.playerNameTextView.text = name
    }

    private fun setPictureOfWord(urlPicture: String) {
        binding.wordCustomCard.setSrcFromAssert(
            urlPicture,
            urlPicture
        )
    }

    private fun setWord(wordCard: CustomWordView.WordCardUI) {
        binding.wordView.setWord(wordCard) {
            CustomLetterView(requireContext()).apply {
                radius = CARD_RADIUS
            }
        }
    }

    private fun requestNextPlayer(state: AnimalLettersState.ChangingState.InvalidLetter) {
        binding.nextPlayerButton.let { button ->
            button.setOnClickListener {
                button.visibility = View.INVISIBLE
                binding.table.setInvalidLetterCard(state.invalidLetterCard)
                viewModel.onClickNextWalkingPlayer()
            }
            button.visibility = View.VISIBLE
        }
    }

    private fun requestNextWord() {
        binding.nextWordButton.visibility = View.VISIBLE
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return false
    }

    private fun initTable(startGameState: AnimalLettersState.EntireState.StartGameState) {
        binding.table.apply {
            setListItem(startGameState.lettersCards) {
                CustomCard(requireContext()).apply {
                    radius = CARD_RADIUS
                    enableClickAnimation()
                    setSrcOpenBackgroundFromAssert("FACE.webp")
                }
            }
            setOnClickListener { pos ->
                viewModel.onClickLetterCard(pos)
            }
        }
    }

    private fun initPictureWord(startGameState: AnimalLettersState.EntireState.StartGameState) {
        binding.wordCustomCard.apply {
            radius = CARD_RADIUS
            //TODO
            setSrcOpenBackgroundFromAssert("FACE.webp")
        }
        setPictureOfWord(startGameState.wordCard.faceImageName)
    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<PlayerInGame>) :
        Parcelable

    companion object {
        const val GAME_START = "GAME_START"

        //Y
        const val CARD_RADIUS = 48f

        @JvmStatic
        fun newInstance(gameStart: GameStart) =
            GameZverobukvyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_START, gameStart)
                }
            }
    }
}