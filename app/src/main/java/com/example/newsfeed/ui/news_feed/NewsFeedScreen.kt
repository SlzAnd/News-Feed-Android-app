package com.example.newsfeed.ui.news_feed

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.newsfeed.R
import com.example.newsfeed.ui.components.BottomNavigationBar
import com.example.newsfeed.ui.components.CustomTopAppBar
import com.example.newsfeed.ui.components.ErrorScreen
import com.example.newsfeed.ui.components.NetworkConnectionMessage
import com.example.newsfeed.ui.components.NewsItem
import com.example.newsfeed.ui.components.ScrollUpIcon
import com.example.newsfeed.ui.navigation.Screen
import com.example.newsfeed.ui.theme.poppinsFontFamily
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "FlowOperatorInvokedInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun NewsFeedScreen(
    navController: NavHostController,
    viewModel: NewsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()

    var connectionWasLost: Boolean? = null

    var isShowNoNetworkMessage by rememberSaveable {
        mutableStateOf(false)
    }

    val snackBarHostState = remember { SnackbarHostState() }

    val lazyColumnState = rememberLazyListState()

    val pullRefreshState = rememberPullRefreshState(state.isLoading, onRefresh = {
        viewModel.onEvent(NewsScreenEvent.UpdateAllNewsFeed)
    })

    coroutineScope.launch {
        viewModel.scrollFlow.collectLatest { needScroll ->
            if (needScroll) {
                lazyColumnState.scrollToItem(index = 0)
            }
        }
    }

    coroutineScope.launch {
        viewModel.connectivityObserver.observe(coroutineScope).collectLatest { isNetworkEnabled ->
            if (isNetworkEnabled && connectionWasLost == true) {
                isShowNoNetworkMessage = false
                connectionWasLost = false
                val result = snackBarHostState.showSnackbar(
                    message = "Network connection available again!",
                    actionLabel = "Get latest news",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onEvent(NewsScreenEvent.UpdateAllNewsFeed)
                    lazyColumnState.scrollToItem(index = 0)
                }
            }

            if (!isNetworkEnabled) {
                isShowNoNetworkMessage = true
                connectionWasLost = true
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                modifier = Modifier,
                hasBookmarkIcon = false,
                isBookmarked = false,
                onBookmarkEvent = {}
            )
        },
        bottomBar = { BottomNavigationBar(navController) },
        snackbarHost = { SnackbarHost(snackBarHostState) }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {

            if (isShowNoNetworkMessage) {
                NetworkConnectionMessage(message = "No network connection")
            }

            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 26.dp, bottom = 10.dp),
                text = stringResource(id = R.string.all_feeds),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )

            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = lazyColumnState
                ) {
                    items(items = state.allNews, key = { it.id }) { news ->
                        NewsItem(
                            modifier = Modifier
                                .testTag("newsItem")
                                .clickable {
                                    navController.navigate(Screen.FullArticle.passUrl(news.id))
                                },
                            news = news,
                            isBookmarked = news.isBookmarked,
                            onBookmarkEvent = {
                                viewModel.onEvent(
                                    NewsScreenEvent.AddToBookmark(
                                        news,
                                        !news.isBookmarked
                                    )
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                PullRefreshIndicator(
                    state.isLoading,
                    pullRefreshState,
                    Modifier
                        .align(Alignment.TopCenter)
                        .testTag("refreshIndicator")
                )

                if (lazyColumnState.canScrollBackward) {
                    ScrollUpIcon(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                    ) {
                        coroutineScope.launch { lazyColumnState.scrollToItem(0) }
                    }
                }
            }
        }

        if (state.isError) {
            ErrorScreen(errorMessage = state.errorMessage)
        }
    }
}