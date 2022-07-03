package com.example.newsapi.view.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.gson.Article
import com.example.newsapi.view.activitys.ArticleActivity
import com.example.newsapi.view.RecyclerArticleAdapter
import com.example.newsapi.viewmodel.NewsListActivityVM

class NewsListFragment : Fragment(), Observer<List<Article>>,
    RecyclerArticleAdapter.OnArticleClickListener {

    private var articlesList = ArrayList<Article>()
    private lateinit var viewModel: NewsListActivityVM
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.recycler_view, container, false)

        adapter = RecyclerArticleAdapter(articlesList, this)
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerView = view.findViewById(R.id.fragment_list_recycler)

        val rvLayoutManager = LinearLayoutManager(this.requireContext())
        val decoration = DividerItemDecoration(this.requireContext(), rvLayoutManager.orientation)

        recyclerView.apply {
            adapter = adapter
            layoutManager = rvLayoutManager
            addItemDecoration(decoration)
        }

        viewModel = ViewModelProvider(requireActivity())[NewsListActivityVM::class.java]
        viewModel.getAllArticles().observe(viewLifecycleOwner, this)

        return view
    }

    override fun onChanged(list: List<Article>) {
        recyclerView.adapter = RecyclerArticleAdapter(list, this)
    }

    override fun onArticleClick(article: Article) {

        // Сохраняем во вьюмодели параметры нажатой статьи
        // чтобы эта статья отображалась в WebView:
        // 1 - В ArticleFragment
        // 2 - При возврате из ArticleActivity

        viewModel.apply {
            title.postValue(article.title)
            author.postValue(article.author)
            publishedAt.postValue(article.publishedAt)
            articleURL.postValue(article.url)
        }
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            val intent = Intent(this.context, ArticleActivity::class.java)
            intent.apply {
                putExtra(ArticleActivity.TITLE_KEY, article.title)
                putExtra(ArticleActivity.AUTHOR_KEY, article.author)
                putExtra(ArticleActivity.PUBLISHED_KEY, article.publishedAt)
                putExtra(ArticleActivity.URL_KEY, article.url)
            }
            startActivity(intent)
        }
    }
}
