package ru.gb.zverobukvy.presentation

import ru.gb.zverobukvy.databinding.BottomSheetDialogFragmentInstructionBinding
import ru.gb.zverobukvy.utility.ui.ViewBindingDialogFragment

class InstructionBottomSheetDialogFragment :
    ViewBindingDialogFragment<BottomSheetDialogFragmentInstructionBinding>(
        BottomSheetDialogFragmentInstructionBinding::inflate
    ) {

    companion object {
        @JvmStatic
        fun instance(): InstructionBottomSheetDialogFragment =
            InstructionBottomSheetDialogFragment()
    }
}