package com.example.newsapi.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.R
import com.example.newsapi.gson.Article

class RecyclerArticleAdapter(
    private val data: List<Article>,
    private val listener: OnArticleClickListener
) : RecyclerView.Adapter<ArticleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ArticleHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ArticleHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleHolder, position: Int) {
        holder.bind(data[position], listener)
    }

    override fun getItemCount(): Int = data.size

    interface OnArticleClickListener {
        fun onArticleClick(article: Article)
    }
}