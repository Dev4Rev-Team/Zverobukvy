package ru.gb.zverobukvy.presentation.animal_letters_game.game_is_over_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
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
        }

        data?.time.also { binding.timeTextView.text = it }

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