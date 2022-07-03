package com.example.newsapi.view

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.gson.Article
import com.squareup.picasso.Picasso

class ArticleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val publishedAt: TextView = itemView.findViewById(R.id.publishedAt)
    private val title: TextView = itemView.findViewById(R.id.title)
    private val description: TextView = itemView.findViewById(R.id.description)
    private lateinit var article: Article
    private val image: ImageView = itemView.findViewById(R.id.image)

    fun bind(article: Article, listener: RecyclerArticleAdapter.OnArticleClickListener) {
        this.article = article

        article.publishedAt = article.publishedAt
            .replace("T", " ") // Убираем лишние знаки
            .replace("Z", "")

        publishedAt.text = article.publishedAt
        title.text = article.title
        description.text = article.description
            .substringBefore("\r") // Убираем лишние знаки
            .replace("<.*?>".toRegex(), "") // Убираем теги
        itemView.setOnClickListener { listener.onArticleClick(article) }

        Picasso
            .get()
            .load(article.urlToImage)
            .into(image)
    }
}
