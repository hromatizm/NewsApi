package com.example.newsapi.model

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.newsapi.gson.NewsResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Загрузка новостных статей в БД
class NewsLoaderService : IntentService("LoadNewsService"), Callback<NewsResult> {

    //    https://newsapi.org/v2/everything?domains=wsj.com&apiKey=01d0c54449784dfcb14f733bfbc256ae
    override fun onHandleIntent(intent: Intent?) {

        val apikey = "01d0c54449784dfcb14f733bfbc256ae"
        val domain = "wsj.com"

        val retrofit = Retrofit.Builder().baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create()).build()

        val service = retrofit.create(NewsServiceRetrofit::class.java)

        val call = service.getNews(apikey, domain)
        call.enqueue(this)
    }

    override fun onResponse(call: Call<NewsResult>, response: Response<NewsResult>) {
        response.body()?.run {
            if (status == "ok") {
                Log.d("zzz", this.status)
                Log.d("zzz", this.totalResults.toString())

                DBWriteUtil.addAllArticles(this.articles)

            } else {
                Log.d("zzz onResponse Fail", this.status)
            }
        }
    }

    override fun onFailure(call: Call<NewsResult>, t: Throwable) {
        Log.d("zzz", "onFailure")
    }
}