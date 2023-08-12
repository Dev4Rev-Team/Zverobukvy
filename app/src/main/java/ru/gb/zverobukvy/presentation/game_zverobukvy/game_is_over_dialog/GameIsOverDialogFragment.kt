package ru.gb.zverobukvy.presentation.game_zverobukvy.game_is_over_dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.gb.zverobukvy.databinding.DialogFragmentGameIsOverBinding
import ru.gb.zverobukvy.utility.parcelable
import ru.gb.zverobukvy.utility.ui.ViewBindingDialogFragment


class GameIsOverDialogFragment :
    ViewBindingDialogFragment<DialogFragmentGameIsOverBinding>(
        DialogFragmentGameIsOverBinding::inflate
    ) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.root.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: GameIsOverDialogData? = arguments?.parcelable(DATE)
        binding.playersRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            data?.let {
                adapter = PlayersRVAdapter(it.list)
            }
        }

        data?.time.also { binding.timeTextView.text = it }

    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentFragmentManager.setFragmentResult(EVENT_CLOSE, bundleOf(EVENT_CLOSE to EVENT_CLOSE))
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "GameIsOverDialogFragment"
        const val EVENT_CLOSE = "GameIsOverDialogFragmentEventClose"
        private const val DATE = "DATE"

        @JvmStatic
        fun instance(data: GameIsOverDialogData): GameIsOverDialogFragment {
            val arg = bundleOf(DATE to data)
            return GameIsOverDialogFragment().apply { arguments = arg }
        }

        fun setOnListenerClose(fragment: Fragment, f: (() -> Unit)?) {
            fragment.parentFragmentManager.setFragmentResultListener(
                EVENT_CLOSE,
                fragment
            ) { _, _ ->
                f?.invoke()
            }
        }
    }

}