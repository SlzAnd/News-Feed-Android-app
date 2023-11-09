package com.example.newsfeed.domain.use_case

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.repository.NewsRepository

class UpdateNewsInCache(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(news: News) {
        return repository.updateNews(news)
    }
}