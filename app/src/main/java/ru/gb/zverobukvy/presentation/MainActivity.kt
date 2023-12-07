package ru.gb.zverobukvy.presentation

import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment
import ru.gb.zverobukvy.utility.ChangeApplicationIcon
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf


class MainActivity : AppCompatActivity() {

    private val viewModel: LoadingDataViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            appComponent.loadingDataViewModel
        })[LoadingDataViewModelImpl::class.java]
    }

    private var isHideSplashScreen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

//todo
//        setTheme(viewModel.getTheme().idTheme)
        setContentView(R.layout.activity_main)
        loadingData()
        volumeControlStream = AudioManager.STREAM_MUSIC
        initMainMenu(savedInstanceState)
        initBottomSheet()
        setHideSplashScreen()
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

    private fun setHideSplashScreen() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else false
                }
            }
        )
    }

    private fun loadingData() {
        viewModel.getLiveDataLoadingData().observe(this) {
            isHideSplashScreen = it
        }
    }
//todo
//    override fun onDestroy() {
//        ChangeApplicationIcon.setIcon(this, ChangeApplicationIcon.IconColour.NEW_YEAR)
//        super.onDestroy()
//    }
}