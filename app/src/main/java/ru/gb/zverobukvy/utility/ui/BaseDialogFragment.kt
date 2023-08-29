package ru.gb.zverobukvy.utility.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import ru.gb.zverobukvy.databinding.DialogFragmentBaseBinding

abstract class BaseDialogFragment: ViewBindingDialogFragment<DialogFragmentBaseBinding>(
    DialogFragmentBaseBinding::inflate
) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        initPositiveButton()
        initNegativeButton()
        initMessageTextView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.attributes = dialog?.window?.attributes?.apply {
            gravity = Gravity.BOTTOM
            // flags = flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
            dimAmount = 0.3f
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

        }
        dialog?.window?.setBackgroundDrawable(
            InsetDrawable(ColorDrawable(Color.TRANSPARENT), 48)
        )
    }

    abstract fun initMessageTextView()

    abstract fun initNegativeButton()

    abstract fun initPositiveButton()
}