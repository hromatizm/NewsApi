package com.example.newsapi.view.activitys

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.R
import com.example.newsapi.view.fragments.ArticleFragment
import com.example.newsapi.viewmodel.ArticleActivityVM

class ArticleActivity : AppCompatActivity() {

    companion object {
        // Ключи для передачи данных
        const val TITLE_KEY = "TITLE_KEY"
        const val AUTHOR_KEY = "AUTHOR_KEY"
        const val PUBLISHED_KEY = "PUBLISHED_KEY"
        const val URL_KEY = "URL_KEY"
    }

    private lateinit var viewModel: ArticleActivityVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setContentView(R.layout.activity_article)

        viewModel = ViewModelProvider(this)[ArticleActivityVM::class.java]
        viewModel.apply {
            title.postValue(intent.getStringExtra(TITLE_KEY).toString())
            author.postValue(intent.getStringExtra(AUTHOR_KEY).toString())
            publishedAt.postValue(intent.getStringExtra(PUBLISHED_KEY).toString())
            articleURL.postValue(intent.getStringExtra(URL_KEY).toString())
        }
    }
}

