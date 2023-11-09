package com.example.newsfeed.domain.use_case

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNewsFromSource(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(sources: List<Source>): Flow<List<News>> {
        return repository.getNews(sources)
    }
}