package com.example.newsfeed.presentation.news_feed

import com.example.newsfeed.domain.model.News

data class NewsState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Error message"
)
