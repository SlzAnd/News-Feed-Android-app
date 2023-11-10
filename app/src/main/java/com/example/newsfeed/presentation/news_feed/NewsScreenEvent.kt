package com.example.newsfeed.presentation.news_feed

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source

sealed class NewsScreenEvent {

    data object UpdateNewsFeed : NewsScreenEvent()
    data class AddToBookmark(val news: News, val isBookmarked: Boolean) : NewsScreenEvent()
    data class AddRemoveNewsSource(val source: Source): NewsScreenEvent()
}
