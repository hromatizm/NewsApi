package com.example.newsapi.model

import com.example.newsapi.gson.NewsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

//https://newsapi.org/v2/everything?domains=wsj.com&apiKey=01d0c54449784dfcb14f733bfbc256ae

interface NewsServiceRetrofit {
    @GET(
        "/v2/everything"
    )
    fun getNews(
        @Query("apiKey")
        apiKey: String,
        @Query("domains")
        domains: String

    ): Call<NewsResult>
}
