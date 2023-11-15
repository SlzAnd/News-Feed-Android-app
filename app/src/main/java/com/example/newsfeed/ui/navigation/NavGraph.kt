package com.example.newsfeed.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsfeed.ui.news_feed.BookmarksScreen
import com.example.newsfeed.ui.article.FullArticleScreen
import com.example.newsfeed.ui.news_feed.GroupsScreen
import com.example.newsfeed.ui.news_feed.NewsFeedScreen
import com.example.newsfeed.ui.news_feed.NewsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    NavHost(navController = navController, startDestination = BottomNavItem.AllNewsFeeds.route) {
        composable(BottomNavItem.AllNewsFeeds.route) {
            NewsFeedScreen(navController = navController, viewModel = viewModel)
        }

        composable(BottomNavItem.Groups.route) {
            GroupsScreen(navController = navController, viewModel = viewModel)
        }

        composable(BottomNavItem.Bookmarked.route) {
            BookmarksScreen(navController = navController, viewModel = viewModel)
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