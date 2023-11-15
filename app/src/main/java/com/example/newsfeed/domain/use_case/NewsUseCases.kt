package com.example.newsfeed.domain.use_case

data class NewsUseCases(
    val getNewsFromSource: GetNewsFromSource,
    val updateNewsInCache: UpdateNewsInCache,
    val refreshNewsFeed: RefreshNewsFeed,
    val getNewsById: GetNewsById,
    val getAllNews: GetAllNews,
)
