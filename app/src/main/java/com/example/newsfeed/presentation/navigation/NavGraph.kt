package com.example.newsfeed.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsfeed.presentation.news_feed.BookmarksScreen
import com.example.newsfeed.presentation.article.FullArticleScreen
import com.example.newsfeed.presentation.news_feed.GroupsScreen
import com.example.newsfeed.presentation.news_feed.NewsFeedScreen

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = BottomNavItem.AllNewsFeeds.route) {
        composable(BottomNavItem.AllNewsFeeds.route) {
            NewsFeedScreen(navController = navController)
        }

        composable(BottomNavItem.Groups.route) {
            GroupsScreen(navController = navController)
        }

        composable(BottomNavItem.Bookmarked.route) {
            BookmarksScreen(navController = navController)
        }

        composable(
            route = Screen.FullArticle.route,
            arguments = listOf(
                navArgument("newsId") {
                    type = NavType.IntType
                }
            )
        ) {
            FullArticleScreen(navController = navController)
        }
    }
}