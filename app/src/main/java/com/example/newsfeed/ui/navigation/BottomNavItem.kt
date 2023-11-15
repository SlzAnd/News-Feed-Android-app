package com.example.newsfeed.ui.navigation

import com.example.newsfeed.R

sealed class BottomNavItem(var icon: Int, var route: String) {
    data object AllNewsFeeds : BottomNavItem(R.drawable.eye, "all_news")
    data object Groups : BottomNavItem(R.drawable.apps, "groups")
    data object Bookmarked : BottomNavItem(R.drawable.bookmark, "bookmarked")
}
