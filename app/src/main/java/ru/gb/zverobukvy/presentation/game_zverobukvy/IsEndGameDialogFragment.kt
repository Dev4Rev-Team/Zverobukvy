package ru.gb.zverobukvy.presentation.game_zverobukvy

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import ru.gb.zverobukvy.databinding.DialogFragmentIsEndGameBinding
import ru.gb.zverobukvy.utility.ui.ViewBindingDialogFragment


class IsEndGameDialogFragment : ViewBindingDialogFragment<DialogFragmentIsEndGameBinding>(
    DialogFragmentIsEndGameBinding::inflate
) {
    var isSendEvent = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding.yesButton.setOnClickListener {
            sendEvent(EVENT_YES)
            dismiss()
        }
        binding.noButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    override fun onDismiss(dialog: DialogInterface) {
        sendEvent(EVENT_NO)
        super.onDismiss(dialog)
    }

    private fun sendEvent(event: String) {
        if (!isSendEvent) {
            isSendEvent = true
            parentFragmentManager.setFragmentResult(event, bundleOf(event to event))
        }
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.attributes = dialog?.window?.attributes?.apply {
            gravity = Gravity.BOTTOM
            // flags = flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            dimAmount = 0.5f
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }

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

