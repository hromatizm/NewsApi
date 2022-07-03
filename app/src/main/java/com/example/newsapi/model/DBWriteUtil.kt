package com.example.newsapi.model

import com.example.newsapi.gson.Article
import java.util.concurrent.Executors

object DBWriteUtil {

    fun addAllArticles(articlesList: List<Article>) {
        articlesList.forEach { article ->
            addArticle(article)
        }
    }

    private fun addArticle(article: Article) {
        val execService = Executors.newFixedThreadPool(1)
        execService.submit(
            AddArticleRunnable(
                NewsDatabase.invoke(MyApplication.getInstance()).newsDao(),
                article
            )
        )
        execService.shutdown()
    }

    class AddArticleRunnable(private val dao: NewsDao, private val article: Article) : Runnable {
        override fun run() {
            dao.addArticle(article)
        }
    }
}