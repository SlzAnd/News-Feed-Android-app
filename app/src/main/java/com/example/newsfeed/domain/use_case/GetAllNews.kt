package com.example.newsfeed.domain.use_case

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetAllNews (
    private val repository: NewsRepository
) {
    operator fun invoke(): Flow<List<News>> {
        return repository.getAllNews()
    }
}