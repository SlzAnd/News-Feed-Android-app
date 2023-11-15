package com.example.newsfeed.domain.repository

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.RefreshResponse
import com.example.newsfeed.domain.model.Source
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface NewsRepository {

    suspend fun getNewsBySource(sources: List<Source>): Flow<List<News>>
    fun getAllNews(): Flow<List<News>>

    suspend fun refreshNews(sources: List<Source>, lastUpdateTime: LocalDateTime?): RefreshResponse

    suspend fun updateNews(news: News)

    suspend fun getNewsById(id: Int): News?
}