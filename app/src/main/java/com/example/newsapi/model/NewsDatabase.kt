package com.example.newsapi.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapi.gson.Article
import com.example.newsapi.gson.SourceTypeConverter

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(SourceTypeConverter::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {

        @Volatile
        private var instance: NewsDatabase? = null

        // для synchronized
        private val LOCK = Object()

        // Для получения БД из MyAplication
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context.applicationContext).also { newsDB ->
                instance = newsDB
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, NewsDatabase::class.java, "news.db")
                .build()
    }
}