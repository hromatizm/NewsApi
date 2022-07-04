package com.example.newsapi.viewmodel

import android.content.res.Configuration
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import com.example.newsapi.gson.Article
import com.example.newsapi.model.MyApplication
import com.example.newsapi.view.RecyclerArticleAdapter

class ActivityViewModel : ViewModel(),
    RecyclerArticleAdapter.OnArticleClickListener {

    // Ссылка на MyApplication для получения: context и resources
    val app = MyApplication.getInstance()

    // Данные о статье, на которые подписан фрагмент со статьей
    val title = MutableLiveData<String>()
    val author = MutableLiveData<String>()
    val publishedAt = MutableLiveData<String>()
    val articleURL = MutableLiveData<String>()

    // Признаки вдимимости фрагментов и линии между ними, на которые подписана активность
    val listFragmentViewVisibility = MutableLiveData<Int>()
    val borderLineVisibility = MutableLiveData<Int>()
    val articleFragmentViewVisibility = MutableLiveData<Int>()

    val transition = MutableLiveData<Transition>()

    private var slide = Slide()
        get() {
            field = Slide(Gravity.LEFT)
            field.duration = 150
            return field
        }

    private var fade = Fade()
        get() {
            field.duration = 500
            return field
        }

    // Технические флаги для определения, того
    // как отображать на экране фрагменты (один из двух или сразу два)
    private var backPressed = false
    private var articleClicked = false
    private var mainActivityFirstRun = true
    private var newsListFragmentOnScreenOnly = false
    private var articleFragmentOnScreenOnly = false

    // Сеттеры для технических флагов
    private fun setOnlyNewsListFragmentOnScreen() {
        newsListFragmentOnScreenOnly = true
        articleFragmentOnScreenOnly = false
    }

    private fun setOnlyArticleFragmentOnScreen() {
        newsListFragmentOnScreenOnly = false
        articleFragmentOnScreenOnly = true
    }

    private fun setBothFragmentOnScreen() {
        newsListFragmentOnScreenOnly = false
        articleFragmentOnScreenOnly = false
    }

    private fun showBothFragments() {
        setBothFragmentOnScreen()
        transition.value = fade
        listFragmentViewVisibility.value = View.VISIBLE
        borderLineVisibility.value = View.VISIBLE
        articleFragmentViewVisibility.value = View.VISIBLE
    }

    private fun showOneFragment() {
        when {
            mainActivityFirstRun -> {
                transition.value = fade
                showNewsListFragmentOnly()
                mainActivityFirstRun = false
            }
            newsListFragmentOnScreenOnly && articleClicked -> {
                transition.value = slide
                showArticleFragmentOnly()
                articleClicked = false
            }
            articleFragmentOnScreenOnly && backPressed -> {
                transition.value = slide
                showNewsListFragmentOnly()
                backPressed = false
            }
            newsListFragmentOnScreenOnly -> {
                transition.value = slide
                showArticleFragmentOnly()
            }
            articleFragmentOnScreenOnly -> {
                transition.value = slide
                showArticleFragmentOnly()
            }
            !newsListFragmentOnScreenOnly && !articleFragmentOnScreenOnly -> {
                transition.value = fade
                showNewsListFragmentOnly()
            }
        }
    }

    private fun showNewsListFragmentOnly() {
        setOnlyNewsListFragmentOnScreen()
        listFragmentViewVisibility.value = View.VISIBLE
        borderLineVisibility.value = View.GONE
        articleFragmentViewVisibility.value = View.GONE
    }

    private fun showArticleFragmentOnly() {
        setOnlyArticleFragmentOnScreen()
        listFragmentViewVisibility.value = View.GONE
        borderLineVisibility.value = View.GONE
        articleFragmentViewVisibility.value = View.VISIBLE
    }

    fun onCreateActivity() {
        when (app.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> showBothFragments()
            Configuration.ORIENTATION_PORTRAIT -> showOneFragment()
            else -> throw Exception("Unknown screen orientation")
        }
        NewsLoaderServiceStarter.start(app.applicationContext)
    }

    fun orientationChangedToPortrait() {
        when {
            articleFragmentOnScreenOnly -> showArticleFragmentOnly()
            else -> showNewsListFragmentOnly()
        }
    }

    fun orientationChangedToLandscape() {
        when {
            articleFragmentOnScreenOnly -> showArticleFragmentOnly()
            else -> showBothFragments()
        }
    }

    override fun onArticleClick(article: Article) {
        articleClicked = true
        title.value = article.title
        author.value = article.author
        publishedAt.value = article.publishedAt
        articleURL.value = article.articleUrl

        val orientation = app.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            showOneFragment()
        }
    }

    fun onBackPressed(): Boolean {
        backPressed = true
        val orientation = MyApplication.getInstance().resources.configuration.orientation
        return when {
            orientation == Configuration.ORIENTATION_LANDSCAPE
                    && articleFragmentOnScreenOnly -> {
                showBothFragments()
                backPressed = false

                false
            }
            orientation == Configuration.ORIENTATION_PORTRAIT
                    && articleFragmentOnScreenOnly -> {
                showOneFragment()

                false
            }
            else ->

                true
        }
    }
}