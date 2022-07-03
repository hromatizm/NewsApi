package com.example.newsapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class ArticleActivityVM : ViewModel() {
    val title = MutableLiveData<String>()
    val author = MutableLiveData<String>()
    val publishedAt = MutableLiveData<String>()
    val articleURL = MutableLiveData<String>()

}