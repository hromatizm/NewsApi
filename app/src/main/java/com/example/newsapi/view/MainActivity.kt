package com.example.newsapi.view

import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.transition.*
import com.example.newsapi.R
import com.example.newsapi.viewmodel.ActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ActivityViewModel
    private lateinit var rootView: ViewGroup
    private lateinit var borderLine: View
    private val mainActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        this.deleteDatabase("news.db")
        // Делаем фул-скрин
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        rootView = findViewById(R.id.root_view)
        borderLine = findViewById(R.id.border_line)

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        observeViewModel()
        viewModel.onCreateActivity()
    }

    private fun observeViewModel() {
        var transitionFragment : Transition? = null

        viewModel.apply {
            transition.observe(mainActivity) { liveData ->
                transitionFragment = liveData

            }

            listFragmentViewVisibility.observe(mainActivity) { liveData ->

                Log.d("zzz listFragmentViewVisibility", transition.toString())
                TransitionManager.beginDelayedTransition(rootView, transitionFragment)
                rootView.findViewById<View>(R.id.container_list).visibility = liveData
            }
            borderLineVisibility.observe(mainActivity) { liveData ->
                borderLine.visibility = liveData
            }
            articleFragmentViewVisibility.observe(mainActivity) { liveData ->
                Log.d("zzz articleFragmentViewVisibility", transition.toString())
                TransitionManager.beginDelayedTransition(rootView, transitionFragment)
                rootView.findViewById<View>(R.id.container_article).visibility = liveData
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewModel.orientationChangedToLandscape()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewModel.orientationChangedToPortrait()
        }
    }

    override fun onBackPressed() {
        if (viewModel.onBackPressed())
            super.onBackPressed()
    }
}





