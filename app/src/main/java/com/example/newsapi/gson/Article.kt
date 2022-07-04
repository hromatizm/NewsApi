package com.example.newsapi.gson

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "articles")
data class Article(

    @SerializedName("author")
    val author: String,
    @SerializedName("description")
    val description: String,
    @PrimaryKey
    @SerializedName("publishedAt")
    var publishedAt: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val articleUrl: String,
    @SerializedName("urlToImage")
    val urlToImage: String
) : Serializable