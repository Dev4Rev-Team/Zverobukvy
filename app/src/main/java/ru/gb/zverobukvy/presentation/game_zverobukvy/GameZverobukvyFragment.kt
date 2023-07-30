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
import ru.gb.zverobukvy.domain.entity.Player
import ru.gb.zverobukvy.domain.entity.TypeCards
import ru.gb.zverobukvy.domain.use_case.AnimalLettersInteractorImpl
import ru.gb.zverobukvy.presentation.AnimalLettersViewModel
import ru.gb.zverobukvy.presentation.AnimalLettersViewModelImpl
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

//        viewModel.getChangingGameStateLiveData().observe(viewLifecycleOwner){
//            when(it){
//                is AnimalLettersChangingState.CorrectLetter -> TODO()
//                is AnimalLettersChangingState.GuessedWord -> TODO()
//                is AnimalLettersChangingState.InvalidLetter -> TODO()
//                is AnimalLettersChangingState.NextPlayer -> TODO()
//            }
//        }
//
//        viewModel.getEntireGameStateLiveData().observe(viewLifecycleOwner){
//            when (it) {
//                is AnimalLettersEntireState.EndGameState -> TODO()
//                is AnimalLettersEntireState.IsEndGameState -> TODO()
//                is AnimalLettersEntireState.LoadingGameState -> TODO()
//                is AnimalLettersEntireState.StartGameState -> TODO()
//            }
//        }

//        viewModel.onActiveGame()
    }

    @Parcelize
    data class GameStart(val typesCards: List<TypeCards>, val players: List<Player>) : Parcelable

    companion object {
        const val GAME_START = "GAME_START"

        @JvmStatic
        fun newInstance(gameStart: GameStart) =
            GameZverobukvyFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_START, gameStart)
                }
            }
    }
}