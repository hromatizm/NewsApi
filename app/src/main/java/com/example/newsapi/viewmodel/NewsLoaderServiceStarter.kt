package com.example.newsapi.viewmodel

import android.content.Context
import android.content.Intent
import com.example.newsapi.model.NewsLoaderService

object NewsLoaderServiceStarter {

    var needToUpdateDB = false // флаг для обновления БД

    fun start(context: Context) {
        if (needToUpdateDB) {
            val service = Intent(context, NewsLoaderService::class.java)
            needToUpdateDB = false
            context.startService(service)
        }
    }
}