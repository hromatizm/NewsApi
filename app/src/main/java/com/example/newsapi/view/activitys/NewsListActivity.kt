package com.example.newsapi.view.activitys

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.R
import com.example.newsapi.viewmodel.NewsListActivityVM
import com.example.newsapi.viewmodel.NewsLoaderServiceStarter

class NewsListActivity : AppCompatActivity() {

    // Вью фрагмента со статьей:
    private lateinit var articleFragmentView: View

    // Граница между фрагментами:
    private lateinit var borderLine: View

    private lateinit var viewModel: NewsListActivityVM

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

        setContentView(R.layout.activity_news_list)

        viewModel = ViewModelProvider(this)[NewsListActivityVM::class.java]

        articleFragmentView = findViewById(R.id.article_fragment)
        borderLine = findViewById(R.id.border_line)
        articleFragmentView.setOnClickListener {

        }
        // Для сохранения состояния WebView
        // в манифесте отключена перерисовка Activity при смене ориентации экрана
        // Поэтому здесь (и в onConfigurationChanged) отрабататывает логика измненения вида
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeArticleFragmentVisible()
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            makeArticleFragmentGone()
        }

        NewsLoaderServiceStarter.start(this.applicationContext)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeArticleFragmentVisible()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            makeArticleFragmentGone()
        }
    }

    private fun makeArticleFragmentVisible() {
        articleFragmentView.visibility = View.VISIBLE
        borderLine.visibility = View.VISIBLE
    }

    private fun makeArticleFragmentGone() {
        articleFragmentView.visibility = View.GONE
        borderLine.visibility = View.GONE
    }

    fun onArticleFragmentClick() {
        Log.d("zzz", "onArticleFragmentClick")
        val intent = Intent(this, ArticleActivity::class.java)
        intent.apply {
            putExtra(ArticleActivity.TITLE_KEY, viewModel.title.value)
            putExtra(ArticleActivity.AUTHOR_KEY, viewModel.author.value)
            putExtra(ArticleActivity.PUBLISHED_KEY, viewModel.publishedAt.value)
            putExtra(ArticleActivity.URL_KEY, viewModel.articleURL.value)
        }
        startActivity(intent)
    }
}



