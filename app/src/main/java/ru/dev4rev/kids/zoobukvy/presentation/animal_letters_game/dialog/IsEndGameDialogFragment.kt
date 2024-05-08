package ru.dev4rev.kids.zoobukvy.presentation.animal_letters_game.dialog

import android.content.DialogInterface
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.utility.ui.BaseDialogFragment


class IsEndGameDialogFragment : BaseDialogFragment() {
    private var event = EVENT_NO

    override fun initMessageTextView() {
        binding.messageTextView.text = getString(R.string.end_game_question_text)
    }

    override fun initNegativeButton() {
        binding.noButton.setOnClickListener {
            dismiss()
        }
    }

    override fun initPositiveButton() {
        binding.yesButton.setOnClickListener {
            event = EVENT_YES
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        parentFragmentManager.setFragmentResult(event, bundleOf())
        super.onDismiss(dialog)
    }

    companion object {
        const val TAG = "IsEndGameDialogFragment"
        const val EVENT_YES = "IsEndGameDialogFragmentYes"
        const val EVENT_NO = "IsEndGameDialogFragmentNo"

        @JvmStatic
        fun instance(): IsEndGameDialogFragment {
            return IsEndGameDialogFragment()
        }

        fun setOnListenerYes(fragment: Fragment, f: (() -> Unit)?) {
            fragment.parentFragmentManager.setFragmentResultListener(
                EVENT_YES, fragment
            ) { _, _ ->
                f?.invoke()
            }
        }

        fun setOnListenerNo(fragment: Fragment, f: (() -> Unit)?) {
            fragment.parentFragmentManager.setFragmentResultListener(
                EVENT_NO, fragment
            ) { _, _ ->
                f?.invoke()
            }
        }
    }

}

