package ru.dev4rev.kids.zoobukvy.presentation

import android.media.AudioManager
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import ru.dev4rev.kids.zoobukvy.R
import ru.dev4rev.kids.zoobukvy.appComponent
import ru.dev4rev.kids.zoobukvy.data.theme_provider.Theme
import ru.dev4rev.kids.zoobukvy.presentation.main_menu.MainMenuFragment
import ru.dev4rev.kids.zoobukvy.utility.ui.viewModelProviderFactoryOf


class MainActivity : AppCompatActivity() {

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

    var backPressedCallback: OnBackPressedCallback? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private fun initBottomSheet() {
        val bottomSheetView = findViewById<View>(R.id.containerBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)
        bottomSheetView.visibility = View.VISIBLE
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        bottomSheetBehavior.addBottomSheetCallback(object :BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN){
                    backPressedCallback?.isEnabled = false
                    MainMenuFragment.setCloseInstruction(this@MainActivity)
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


        MainMenuFragment.setOnListenerShowInstruction(this) {
            val bottomFragment =
                InstructionBottomSheetDialogFragment.instance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.containerBottomSheet, bottomFragment)
                .commitAllowingStateLoss()

            backPressedCallback?.remove()
            backPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            backPressedCallback?.let{onBackPressedDispatcher.addCallback( this, it)}
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

        InstructionBottomSheetDialogFragment.setOnListenerClickClose(this) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
}