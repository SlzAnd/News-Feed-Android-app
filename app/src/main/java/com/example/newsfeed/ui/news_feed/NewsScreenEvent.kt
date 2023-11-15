package com.example.newsfeed.ui.news_feed

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source

sealed class NewsScreenEvent {
    data object UpdateAllNewsFeed : NewsScreenEvent()
    data object UpdateNewsBySourcesFeed : NewsScreenEvent()
    data class AddToBookmark(val news: News, val isBookmarked: Boolean) : NewsScreenEvent()
    data class AddRemoveNewsSource(val source: Source) : NewsScreenEvent()
}
