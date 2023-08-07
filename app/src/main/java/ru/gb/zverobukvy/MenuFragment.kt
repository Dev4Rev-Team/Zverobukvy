package ru.gb.zverobukvy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.gb.zverobukvy.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.customCardButton.setOnClickListener {
            goTo(TestCustomCardFragment.newInstance())
        }
        binding.customCardTableButton.setOnClickListener {
            goTo(TestCustomCardTableFragment.newInstance())
        }
        binding.customLetterView.setOnClickListener {
            goTo(TestCustomLetterViewFragment.newInstance())
        }

        binding.customWordView.setOnClickListener {
            goTo(TestCustomWordViewFragment.newInstance())
        }

        return binding.root
    }

    private fun goTo(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack("ok")
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MenuFragment()
    }
}