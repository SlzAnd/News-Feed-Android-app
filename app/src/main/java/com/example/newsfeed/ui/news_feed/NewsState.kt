package com.example.newsfeed.ui.news_feed

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source

data class NewsState(
    val news: List<News> = emptyList(),
    val allNews: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "Error message",
    val sources: Map<String, Boolean> = mapOf(
        Source.FOX_NEWS.name to false,
        Source.NYT.name to false,
        Source.DEV_UA.name to false,
        Source.THE_GUARDIAN.name to false,
        Source.SKY_SPORTS.name to false,
    ),
)
