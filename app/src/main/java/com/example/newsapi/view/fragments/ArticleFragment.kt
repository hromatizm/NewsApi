package com.example.newsapi.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.R
import com.example.newsapi.viewmodel.ArticleActivityVM
import com.example.newsapi.viewmodel.NewsListActivityVM

class ArticleFragment : Fragment() {

    // Виджеты
    private lateinit var titleView: TextView
    private lateinit var authorView: TextView
    private lateinit var publishedAtView: TextView
    private lateinit var webView: WebView

    private lateinit var viewModel: ArticleActivityVM

    // Флаги для инициализации подписки на вьюмодель
    // Чтобы не перегружать WebView при каждом повороте экрана
    var urlLoadedToNewsListActivityVM = false
        private set
    var urlLoadedToArticleActivityVM = false
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_article, container, false)
        titleView = view.findViewById(R.id.title) as TextView
        authorView = view.findViewById(R.id.author) as TextView
        publishedAtView = view.findViewById(R.id.publishedAt) as TextView
        webView = view.findViewById(R.id.web) as WebView

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: WebResourceRequest?
            ): Boolean {
                return false
            }
        }
        viewModelInit()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeHeaderGone()
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            makeHeaderVisible()
        }

        return view
    }

    // Для сохранения состояния WebView
    // в манифесте отключена перерисовка ArticleActivity при смене ориентации экрана
    // Поэтому здесь отрабататывает логика измненения вида
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        viewModelInit()

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            makeHeaderGone()
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            makeHeaderVisible()
        }
    }

    private fun viewModelInit() {

        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewModel = ViewModelProvider(requireActivity())[NewsListActivityVM::class.java]
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewModel = ViewModelProvider(requireActivity())[ArticleActivityVM::class.java]
        }

        val viewModelClass = viewModel.javaClass.simpleName
// Чтобы WebView не перегружалось при каждом повороте экрана (и сохранялось его состояние)
// Инициализируем подписку на каждую вьюмодель только один раз по флагу
        when {
            viewModelClass == "NewsListActivityVM"
                    && !urlLoadedToNewsListActivityVM -> {
                applyViewModel()
                urlLoadedToNewsListActivityVM = true
            }
            viewModelClass == "ArticleActivityVM"
                    && !urlLoadedToArticleActivityVM -> {
                applyViewModel()
                urlLoadedToArticleActivityVM = true
            }
        }
    }

    private fun applyViewModel() {
        viewModel.apply {
            title.observe(viewLifecycleOwner) { liveData ->
                titleView.text = liveData
            }
            author.observe(viewLifecycleOwner) { liveData ->
                authorView.text = liveData
            }
            publishedAt.observe(viewLifecycleOwner) { liveData ->
                publishedAtView.text = liveData
            }
            articleURL.observe(viewLifecycleOwner) { liveData ->
                webView.loadUrl(liveData)
            }
        }
    }

    private fun makeHeaderGone(){
        titleView.visibility = View.GONE
        authorView.visibility = View.GONE
        publishedAtView.visibility = View.GONE
    }

    private fun makeHeaderVisible(){
        titleView.visibility = View.VISIBLE
        authorView.visibility = View.VISIBLE
        publishedAtView.visibility = View.VISIBLE
    }
}
