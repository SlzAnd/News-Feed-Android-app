package com.example.newsfeed.presentation.news_feed

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source

data class NewsState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Error message",
    val sources: Map<String, Boolean> = mapOf(
        Source.FOX_NEWS.name to true,
        Source.NYT.name to true,
        Source.DEV_UA.name to true,
        Source.THE_GUARDIAN.name to true,
        Source.SKY_SPORTS.name to true,
    )
)
