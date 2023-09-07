package ru.gb.zverobukvy.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMainMenu(savedInstanceState)
        initBottomSheet()
    }

    private fun initMainMenu(savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.container,
                    MainMenuFragment.newInstance(),
                    MainMenuFragment.TAG_MAIN_MENU_FRAGMENT
                )
                .commitAllowingStateLoss()
    }

    private fun initBottomSheet() {
        val bottomSheetView = findViewById<View>(R.id.containerBottomSheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)

        MainMenuFragment.setOnListenerShowInstruction(this) {
            bottomSheetView.visibility = View.VISIBLE
            val bottomFragment =
                InstructionBottomSheetDialogFragment.instance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commitAllowingStateLoss()
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        InstructionBottomSheetDialogFragment.setOnListenerClickHeader(this) {
            bottomSheetBehavior.state = when (bottomSheetBehavior.state) {
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    BottomSheetBehavior.STATE_EXPANDED
                }

                BottomSheetBehavior.STATE_EXPANDED -> {
                    BottomSheetBehavior.STATE_HIDDEN
                }

                else -> bottomSheetBehavior.state
            }

        }

    }
}