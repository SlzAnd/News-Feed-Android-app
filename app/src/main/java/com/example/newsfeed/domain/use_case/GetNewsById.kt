package com.example.newsfeed.domain.use_case

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.repository.NewsRepository

class GetNewsById(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(id: Int): News? {
        return repository.getNewsById(id)
    }
}