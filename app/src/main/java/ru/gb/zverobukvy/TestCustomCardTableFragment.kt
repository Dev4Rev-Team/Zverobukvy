package ru.gb.zverobukvy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.gb.zverobukvy.databinding.FragmentTestCustomCardTableBinding
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomCardTable

class TestCustomCardTableFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentTestCustomCardTableBinding.inflate(inflater, container, false)
        initUI(binding)
        return binding.root
    }

    private fun initUI(binding: FragmentTestCustomCardTableBinding) {
        binding.setListButton.setOnClickListener {
            val letterCardList = listOf(
                LetterCard('A', false, "img7.png"),
                LetterCard('B', false, "img2.png"),
                LetterCard('C', false, "img3.png"),
                LetterCard('D', true, "img4.png"),
                LetterCard('E', false, "img5.png"),
                LetterCard('F', false, "img6.png"),
                LetterCard('V', true, "img7.png"),
                LetterCard('A', true, "img8.png"),
                LetterCard('A', false, "img9.png"),
                LetterCard('A', false, "img10.png"),
                LetterCard('C', false, "img3.png"),
                LetterCard('D', true, "img4.png"),
                LetterCard('E', false, "img5.png"),
                LetterCard('F', false, "img6.png"),
                LetterCard('A', true, "img7.png"),
                LetterCard('A', true, "img8.png"),
                LetterCard('A', false, "img9.png"),
                LetterCard('A', false, "img10.png"),
                LetterCard('E', false, "img5.png"),
                LetterCard('F', false, "img6.png"),
                LetterCard('A', true, "img7.png"),
                LetterCard('A', true, "img8.png"),
                LetterCard('A', false, "img9.png"),
                LetterCard('A', false, "img10.png"),
                LetterCard('C', false, "img3.png"),
                LetterCard('D', true, "img4.png"),
                LetterCard('E', false, "img5.png"),
                LetterCard('F', false, "img6.png"),
                LetterCard('A', true, "img7.png"),
                LetterCard('A', true, "img8.png"),
                LetterCard('A', false, "img9.png"),
                LetterCard('A', false, "img10.png"),
                LetterCard('A', false, "img8.png"),

            )
            binding.cardTable.setListItem(letterCardList, "img1.png") {
                CustomCard(requireContext()).apply {
                    radius = 50f
                    elevation = 3f
                }
            }
        }

        binding.correctButton.setOnClickListener {
            binding.cardTable.setCorrectLetterCard(LetterCard('V', false, "img7.png"))
        }

        binding.invalidButton.setOnClickListener {
            binding.cardTable.setInvalidLetterCard(LetterCard('V', true, "img7.png"))
        }

        binding.nextPlayerButton.setOnClickListener {
            binding.cardTable.nextPlayer()
        }

        binding.nextWordButton.setOnClickListener {
            binding.cardTable.nextWord()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            TestCustomCardTableFragment()
    }

     data class LetterCard(
         override val letter: Char, // equals
         override var isVisible: Boolean = false,
         override val url: String,
    ): CustomCardTable.LetterCardUI
}