package com.example.newsapi.gson

import com.google.gson.annotations.SerializedName

data class NewsResult(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)