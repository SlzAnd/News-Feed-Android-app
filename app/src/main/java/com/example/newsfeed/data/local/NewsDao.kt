package com.example.newsfeed.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Upsert
    suspend fun saveNews(news: List<News>)

    @Update
    suspend fun updateNews(news: News)

    @Query("SELECT * FROM news WHERE news.newsSource = :source")
    fun getNewsBySource(source: Source): Flow<List<News>>

    @Query("SELECT * FROM news WHERE news.id = :id")
    suspend fun getNewsById(id: Int): News?

    @Query("DELETE FROM news")
    suspend fun deleteAll()
}