package com.example.newsfeed.presentation.news_feed

import com.example.newsfeed.domain.model.News

sealed class NewsScreenEvent {

    data object UpdateNewsFeed : NewsScreenEvent()
    data class AddToBookmark(val news: News, val isBookmarked: Boolean) : NewsScreenEvent()
}
