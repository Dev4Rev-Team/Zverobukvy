package ru.gb.zverobukvy.presentation

import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import ru.gb.zverobukvy.R
import ru.gb.zverobukvy.appComponent
import ru.gb.zverobukvy.data.theme_provider.Theme
import ru.gb.zverobukvy.presentation.main_menu.MainMenuFragment
import ru.gb.zverobukvy.utility.ui.viewModelProviderFactoryOf


class MainActivity : AppCompatActivity() {
    private lateinit var backPressedCallback: OnBackPressedCallback

    private val viewModel: LoadingDataViewModel by lazy {
        ViewModelProvider(this, viewModelProviderFactoryOf {
            appComponent.loadingDataViewModel
        })[LoadingDataViewModelImpl::class.java]
    }

    private var isHideSplashScreen = false

    private lateinit var actualTheme: Theme

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        actualTheme = viewModel.getTheme()
        setTheme(actualTheme.idTheme)
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

    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private fun initBottomSheet() {
        val bottomSheetView = findViewById<View>(R.id.containerBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        bottomSheetView.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN


        MainMenuFragment.setOnListenerShowInstruction(this) {
            val bottomFragment =
                InstructionBottomSheetDialogFragment.instance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commitAllowingStateLoss()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            backPressedCallback.isEnabled = true
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
            if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN){
                backPressedCallback.isEnabled = false
            }
        }

        InstructionBottomSheetDialogFragment.setOnListenerClickClose(this) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            backPressedCallback.isEnabled = false
        }

        backPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    isEnabled = false
                }
            }
        }

        onBackPressedDispatcher.addCallback( this, backPressedCallback)
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
}