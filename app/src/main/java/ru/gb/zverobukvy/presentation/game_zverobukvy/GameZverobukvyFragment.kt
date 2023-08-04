package ru.gb.zverobukvy.presentation.game_zverobukvy

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.parcelize.Parcelize
import ru.gb.zverobukvy.data.data_source.LetterCardsDB
import ru.gb.zverobukvy.data.data_source.WordCardsDB
import ru.gb.zverobukvy.data.data_source_impl.LetterCardsDBImpl
import ru.gb.zverobukvy.data.data_source_impl.WordCardsDBImpl
import ru.gb.zverobukvy.data.repository_impl.AnimalLettersCardsRepositoryImpl
import ru.gb.zverobukvy.databinding.FragmentGameZverobukvyBinding
import ru.gb.zverobukvy.domain.app_state.AnimalLettersState
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractorImpl
import ru.gb.zverobukvy.presentation.AnimalLettersViewModel
import ru.gb.zverobukvy.presentation.AnimalLettersViewModelImpl
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf

class GameZverobukvyFragment :
    ViewBindingFragment<FragmentGameZverobukvyBinding>(FragmentGameZverobukvyBinding::inflate) {
    private var gameStart: GameStart? = null

    private val viewModel: AnimalLettersViewModel by lazy {
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
            AnimalLettersViewModelImpl(game)
        })[AnimalLettersViewModelImpl::class.java]
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

        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersState.ChangingState.CorrectLetter -> {
                    binding.table.setCorrectLetterCard()
                }

                is AnimalLettersState.ChangingState.GuessedWord -> {//TODO
                    Toast.makeText(requireContext(), "GuessedWord", Toast.LENGTH_SHORT).show()
                }

                is AnimalLettersState.ChangingState.InvalidLetter -> {
                    binding.table.setInvalidLetterCard(it.invalidLetterCard)
                }

                is AnimalLettersState.ChangingState.NextGuessWord -> {
                    binding.table.nextWord()
                }

                is AnimalLettersState.ChangingState.NextPlayer -> {
                    binding.table.nextPlayer()
                }
            }
        }

        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AnimalLettersState.EntireState.EndGameState -> { //TODO
                    Toast.makeText(requireContext(), "Валим", Toast.LENGTH_SHORT).show()
                }

                is AnimalLettersState.EntireState.IsEndGameState -> {//TODO
                    Toast.makeText(requireContext(), "Let's go", Toast.LENGTH_SHORT).show()
                }

                is AnimalLettersState.EntireState.LoadingGameState -> {//TODO
                    Toast.makeText(requireContext(), "LoadingGameState", Toast.LENGTH_SHORT).show()
                }

                is AnimalLettersState.EntireState.StartGameState -> {
                    initWordCard(it)
                    initTable(it)
                }
            }
        }

//        viewModel.onActiveGame()
    }

    private fun initTable(startGameState: AnimalLettersState.EntireState.StartGameState) {
        binding.table.apply {
            setListItem(startGameState.lettersCards) {
                CustomCard(requireContext()).apply {
                    radius = CARD_RADIUS
                    //TODO
                    setSrcOpenBackgroundFromAssert("FACE.webp")
                }
            }
            setOnClickListener { pos ->
                viewModel.onClickLetterCard(pos)
            }
        }
    }

    private fun initWordCard(startGameState: AnimalLettersState.EntireState.StartGameState) {
        binding.card.apply {
            setSrcFromAssert(
                startGameState.wordCard.faceImageName,
                startGameState.wordCard.faceImageName
            )
            radius = CARD_RADIUS
            setSrcOpenBackgroundFromAssert("FACE.webp")
        }
    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<Player>) : Parcelable

    companion object {
        const val GAME_START = "GAME_START"
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