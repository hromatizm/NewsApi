package com.example.newsapi.viewmodel

import androidx.lifecycle.LiveData
import com.example.newsapi.gson.Article
import com.example.newsapi.model.MyApplication
import com.example.newsapi.model.NewsDatabase

class NewsListActivityVM : ArticleActivityVM() {

    fun getAllArticles(): LiveData<List<Article>> {
        return NewsDatabase.invoke(MyApplication.getInstance()).newsDao().getAllArticles()
    }
}