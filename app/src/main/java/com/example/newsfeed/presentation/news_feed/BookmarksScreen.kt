package com.example.newsfeed.presentation.news_feed

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.newsfeed.R
import com.example.newsfeed.presentation.components.BottomNavigationBar
import com.example.newsfeed.presentation.components.CustomTopAppBar
import com.example.newsfeed.presentation.components.NetworkConnectionMessage
import com.example.newsfeed.presentation.components.NewsItem
import com.example.newsfeed.presentation.navigation.Screen
import com.example.newsfeed.ui.theme.poppinsFontFamily
import com.example.newsfeed.util.ConnectivityObserver
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BookmarksScreen(
    navController: NavHostController,
    viewModel: NewsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var initialStatus by remember { mutableStateOf<ConnectivityObserver.Status?>(null) }
    val connectivityStatus by viewModel.connectivityObserver.observe().collectAsState(
        initial = initialStatus ?: ConnectivityObserver.Status.Unavailable
    )
    if (initialStatus == null) {
        initialStatus = connectivityStatus
    }
    var previousStatus = ConnectivityObserver.Status.Available

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
        ) {
            // No Internet connection -> show message
            if (connectivityStatus == ConnectivityObserver.Status.Lost ||
                connectivityStatus == ConnectivityObserver.Status.Unavailable
            ) {
                previousStatus = connectivityStatus
                NetworkConnectionMessage(message = stringResource(id = R.string.no_network))

                // Internet was reconnected -> show snackbar message with refreshing news possibility
            } else if (connectivityStatus == ConnectivityObserver.Status.Available &&
                (previousStatus == ConnectivityObserver.Status.Lost ||
                        previousStatus == ConnectivityObserver.Status.Unavailable)
            ) {
                coroutineScope.launch {
                    val result = snackBarHostState.showSnackbar(
                        message = "Network connection available again!",
                        actionLabel = "Get latest news",
                        duration = SnackbarDuration.Short
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NewsScreenEvent.UpdateNewsFeed)
                    }
                }
                previousStatus = connectivityStatus
            }

            Text(
                modifier = Modifier
                    .padding(top = 16.dp, start = 26.dp, bottom = 10.dp),
                text = stringResource(id = R.string.saved),
                fontFamily = poppinsFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(items = state.news.filter { it.isBookmarked }, key = { it.url }) { news ->
                    NewsItem(
                        modifier = Modifier
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
        }
    }
}