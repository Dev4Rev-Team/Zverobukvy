package ru.gb.zverobukvy.utility.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

abstract class BackPressedFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.let {
            it.addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    //TODO
                    Toast.makeText(requireContext(), "pressBack", Toast.LENGTH_SHORT).show()

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