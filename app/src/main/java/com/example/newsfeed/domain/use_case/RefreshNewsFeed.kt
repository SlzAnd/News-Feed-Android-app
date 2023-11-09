package com.example.newsfeed.domain.use_case

import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.repository.NewsRepository
import java.time.LocalDateTime

class RefreshNewsFeed(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(sources: List<Source>, lastUpdateTime: LocalDateTime?) {
        repository.refreshNews(sources, lastUpdateTime)
    }
}