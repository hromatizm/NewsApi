package com.example.newsapi.gson

import com.google.gson.annotations.SerializedName

data class NewsResult(
    @SerializedName("articles")
    val articles: List<Article>,
    val status: String
)