package com.example.newsapi.viewmodel

import android.content.res.Configuration
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapi.model.MyApplication

class ArticleFragmentViewModel : ViewModel() {

    // Ссылка на MyApplication для получения resources
    val app = MyApplication.getInstance()

    // Признаки вдимимости виджетов, на которые подписан фрагмент
    val titleViewVisibility = MutableLiveData<Int>()
    val authorViewVisibility = MutableLiveData<Int>()
    val publishedAtViewVisibility = MutableLiveData<Int>()

    fun onCreateView() {
        when (app.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> makeHeaderGone()
            Configuration.ORIENTATION_PORTRAIT -> makeHeaderVisible()
            else -> throw Exception("Unknown screen orientation")
        }
    }

    fun orientationChangedToLandscape() {
        makeHeaderGone()
    }

    fun orientationChangedToPortrait() {
        makeHeaderVisible()
    }

    private fun makeHeaderGone() {
        titleViewVisibility.postValue(View.GONE)
        authorViewVisibility.postValue(View.GONE)
        publishedAtViewVisibility.postValue(View.GONE)
    }

    private fun makeHeaderVisible() {
        titleViewVisibility.postValue(View.VISIBLE)
        authorViewVisibility.postValue(View.VISIBLE)
        publishedAtViewVisibility.postValue(View.VISIBLE)
    }
}