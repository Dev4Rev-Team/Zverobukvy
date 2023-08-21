package ru.gb.zverobukvy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.gb.zverobukvy.databinding.FragmentTestCustomCardTableBinding
import ru.gb.zverobukvy.presentation.customview.CustomCard
import ru.gb.zverobukvy.presentation.customview.CustomCardTable
import kotlin.random.Random

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
            val letterCardListTmp = listOf(
                LetterCard('A', false, "img7.png", "img3.png"),
                LetterCard('B', false, "img2.png", "img3.png"),
                LetterCard('C', false, "img3.png", "img3.png"),
                LetterCard('D', true, "img4.png", "img3.png"),
                LetterCard('E', false, "img5.png", "img3.png"),
                LetterCard('F', false, "img6.png", "img3.png"),
                LetterCard('V', true, "img7.png", "img3.png"),
                LetterCard('A', true, "img8.png", "img3.png"),
                LetterCard('A', false, "img9.png", "img3.png"),
            )

            val countCard = binding.editTextNumber.text.toString().toInt()
            val letterCardList = mutableListOf<LetterCard>()
            repeat(countCard) {
                letterCardList.add(
                    letterCardListTmp[
                            Random.nextInt(letterCardListTmp.size)]
                )
            }
            val fakeAssetsImageCash = FakeAssetsImageCash(requireContext())
            binding.cardTable.setListItem(letterCardList, fakeAssetsImageCash) {
                CustomCard(requireContext()).apply {
                    radius = 50f
                    elevation = 3f
                }
            }
        }

        binding.correctButton.setOnClickListener {
            binding.cardTable.setCorrectLetterCard()
        }

        binding.invalidButton.setOnClickListener {
            binding.cardTable.setInvalidLetterCard(
                LetterCard('V', true, "img7.png", "img3.png")
            )
        }

        binding.nextPlayerButton.setOnClickListener {
            binding.cardTable.nextPlayer()
        }

        binding.nextWordButton.setOnClickListener {
            binding.cardTable.nextWord()
        }

        binding.cardTable.setOnClickListener {
            Toast.makeText(requireContext(), "pos $it", Toast.LENGTH_SHORT).show()
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
        override val faceImageName: String,
        override val backImageName: String,
    ) : CustomCardTable.LetterCardUI
}