package com.example.newsapi.view.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.newsapi.R
import com.example.newsapi.viewmodel.ActivityViewModel
import com.example.newsapi.viewmodel.ArticleFragmentViewModel

class ArticleFragment : Fragment() {

    private lateinit var titleView: TextView
    private lateinit var authorView: TextView
    private lateinit var publishedAtView: TextView
    private lateinit var webView: WebView

    private lateinit var activityViewModel: ActivityViewModel
    private lateinit var fragmentViewModel: ArticleFragmentViewModel

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

        setupWebView()

        activityViewModel =
            ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
        observeActivityViewModel()

        fragmentViewModel =
            ViewModelProvider(requireActivity())[ArticleFragmentViewModel::class.java]
        observeFragmentViewModel()
        fragmentViewModel.onCreateView()

        return view
    }

    // Для сохранения состояния WebView
    // в манифесте отключена перерисовка ArticleActivity при смене ориентации экрана
    // Поэтому здесь отрабататывает логика измненения вида
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            fragmentViewModel.orientationChangedToLandscape()
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            fragmentViewModel.orientationChangedToPortrait()
    }

    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            // Чтобы страница открывалась не в стороннем браузере:
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url: WebResourceRequest?
            ): Boolean {
                return false
            }
            // Сохраняем Cookie
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().apply {
                    setAcceptCookie(true)
                    acceptCookie()
                    flush()
                }
            }
        }
        webView.settings.apply {
            // Поддержка JS:
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            // Поддержка localStorage:
            databaseEnabled = true
            domStorageEnabled = true
            // Кэш:
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        }
    }

    private fun observeActivityViewModel() {
        activityViewModel.apply {
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

    private fun observeFragmentViewModel() {
        fragmentViewModel.apply {
            titleViewVisibility.observe(viewLifecycleOwner) { liveData ->
                titleView.visibility = liveData
            }
            authorViewVisibility.observe(viewLifecycleOwner) { liveData ->
                authorView.visibility = liveData
            }
            publishedAtViewVisibility.observe(viewLifecycleOwner) { liveData ->
                publishedAtView.visibility = liveData
            }
        }
    }
}
