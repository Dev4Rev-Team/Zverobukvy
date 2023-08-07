package ru.gb.zverobukvy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gb.zverobukvy.databinding.FragmentTestCustomWordViewBinding
import ru.gb.zverobukvy.presentation.customview.CustomLetterView
import ru.gb.zverobukvy.presentation.customview.CustomWordView

class TestCustomWordViewFragment : Fragment() {
    lateinit var binding:FragmentTestCustomWordViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTestCustomWordViewBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val word = object : CustomWordView.WordCardUI{
            override val word: String
                get() = "КРУТG"
            override val positionsGuessedLetters: MutableList<Int>
                get() = mutableListOf(3)
        }
        binding.setWordButton.setOnClickListener {
            binding.customWordView.setWord(word) {
                CustomLetterView(requireContext()).apply {
                    radius = 24f
                }
            }
        }

        binding.newPosButton.setOnClickListener {
            binding.customWordView.setPositionLetterInWord(1)
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() =
            TestCustomWordViewFragment()
    }
}