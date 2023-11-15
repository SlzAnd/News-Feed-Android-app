package com.example.newsfeed.ui.navigation

sealed class Screen(val route: String) {

    data object FullArticle : Screen("article_screen/{newsId}") {
        fun passUrl(newsId: Int): String {
            return this.route.replace(
                oldValue = "{newsId}",
                newValue = newsId.toString()
            )
        }
    }
}
