package com.example.newsapi.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapi.gson.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addArticle(vararg articles: Article)

    @Delete
    fun deleteArticle(vararg articles: Article)

    @Query("SELECT * FROM articles WHERE publishedAt = :publishedAt ORDER BY publishedAt DESC")
    fun getArticle(publishedAt: String): LiveData<List<Article>>

    @Query("SELECT * FROM articles ORDER BY publishedAt DESC")
    fun getAllArticles(): LiveData<List<Article>>

}