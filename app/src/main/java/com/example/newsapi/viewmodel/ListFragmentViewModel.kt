package com.example.newsapi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.newsapi.gson.Article
import com.example.newsapi.model.MyApplication
import com.example.newsapi.model.NewsDatabase

class ListFragmentViewModel : ViewModel() {

    // Список статей, содеражихся в БД, на которые подписан фрагмент со списком
    fun getAllArticles(): LiveData<List<Article>> {
        return NewsDatabase.invoke(MyApplication.getInstance()).newsDao().getAllArticles()
    }
}