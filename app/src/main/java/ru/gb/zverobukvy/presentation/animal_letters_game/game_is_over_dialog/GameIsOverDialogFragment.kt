package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gb.zverobukvy.animalLettersGameSubcomponentContainer
import ru.gb.zverobukvy.databinding.DialogFragmentGameIsOverBinding
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingFragment


class GameIsOverDialogFragment :
    ViewBindingFragment<DialogFragmentGameIsOverBinding>(
        DialogFragmentGameIsOverBinding::inflate
    ) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: DataGameIsOverDialog? = arguments?.parcelable(DATE)
        binding.playersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            data?.let {
                adapter = PlayersRVAdapter(it.list)
            }
        }

        binding.okButton.setOnClickListener {
            parentFragmentManager.popBackStack()
            requireContext().animalLettersGameSubcomponentContainer.deleteAnimalLettersGameSubcomponent()
        }

        data?.time.also { binding.timeTextView.text = it }
        //todo deleter test
//        data?.let {
//            it.list[0].scoreInCurrentGame = 1
//            it.list[1].scoreInCurrentGame = 2
//            it.list[2].scoreInCurrentGame = 3
//            it.list[3].scoreInCurrentGame = 1
//            it.list[4].scoreInCurrentGame = 2
//            it.list[5].scoreInCurrentGame = 2
//            it.list[6].scoreInCurrentGame = 2
//            it.list[7].scoreInCurrentGame = 2
//            it.list[8].scoreInCurrentGame = 4
//            it.list[9].scoreInCurrentGame = 4
//            it.list[10].scoreInCurrentGame = 4
//            it.list[11].scoreInCurrentGame = 4
//            it.list[12].scoreInCurrentGame = 4
//            it.list[13].scoreInCurrentGame = 4
//            it.list[14].scoreInCurrentGame = 4
//        }
        data?.let {
            binding.winnerAvatarCustomImageRatingView.show(it.list)
//            binding.winnerAvatarCustomImageRatingView.show(it.list.sortedByDescending { srt -> srt.scoreInCurrentGame })
        }
    }

    companion object {
        const val TAG = "GameIsOverDialogFragment"
        private const val DATE = "DATE"

        @JvmStatic
        fun instance(data: DataGameIsOverDialog): GameIsOverDialogFragment {
            val arg = bundleOf(DATE to data)
            return GameIsOverDialogFragment().apply { arguments = arg }
        }
    }

}