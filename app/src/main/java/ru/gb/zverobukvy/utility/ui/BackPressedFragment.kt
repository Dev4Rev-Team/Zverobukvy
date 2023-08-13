package ru.gb.zverobukvy.utility.ui

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BackPressedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.let {
            it.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (onBackPressed()) {
                        isEnabled = false
                        it.onBackPressed()
                    }
                }
            })
        }
    }


    open fun onBackPressed(): Boolean = true
}