package com.example.newsfeed.ui.news_feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.RefreshResponse
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.use_case.NewsUseCases
import com.example.newsfeed.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewsViewModel @Inject constructor(
    private val useCases: NewsUseCases,
    dataStore: StoreSettings,
    val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private var allSources = Source.entries.toList()
    private val sources = MutableStateFlow(emptyList<Source>())
    private var lastUpdateTime: LocalDateTime? = null

    private val newsListBySources = sources
        .flatMapLatest { sourceList ->
            useCases.getNewsFromSource.invoke(sourceList)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val allNews = useCases.getAllNews.invoke()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private var _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState())
    val state: StateFlow<NewsState> =
        combine(_state, newsListBySources, allNews) { state, newsListBySources, allNews ->
            state.copy(
                news = newsListBySources.sortedByDescending(News::pubDate),
                allNews = allNews.sortedByDescending(News::pubDate)
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewsState())


    private val _scrollFlow: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val scrollFlow: SharedFlow<Boolean> = _scrollFlow

    init {
        lastUpdateTime = LocalDateTime.parse(dataStore.getLastUpdateTime())
        viewModelScope.launch(Dispatchers.IO) {
            updateNewsFeed(allSources)
        }
    }

    fun onEvent(event: NewsScreenEvent) {
        when (event) {
            is NewsScreenEvent.AddToBookmark -> {
                val updatedNewsItem = event.news.copy(
                    isBookmarked = event.isBookmarked
                )

                viewModelScope.launch(Dispatchers.IO) {
                    useCases.updateNewsInCache.invoke(updatedNewsItem)
                    useCases.getNewsFromSource(sources.value).collectLatest { updatedList ->
                        _state.value = _state.value.copy(
                            news = updatedList
                        )
                    }
                }
            }

            NewsScreenEvent.UpdateAllNewsFeed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateNewsFeed(allSources)
                }
            }

            NewsScreenEvent.UpdateNewsBySourcesFeed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateNewsFeed(sources.value)
                }
            }

            is NewsScreenEvent.AddRemoveNewsSource -> {
                if (event.source in sources.value) {
                    val updatedSourceList = sources.value.toMutableList()
                    updatedSourceList.remove(event.source)
                    sources.value = updatedSourceList

                    val updatedSourceMap = _state.value.sources.toMutableMap()
                    updatedSourceMap[event.source.name] = false
                    _state.value = _state.value.copy(
                        sources = updatedSourceMap
                    )
                } else {
                    val updatedSourceList = sources.value.toMutableList()
                    updatedSourceList.add(event.source)
                    sources.value = updatedSourceList

                    val updatedSourceMap = _state.value.sources.toMutableMap()
                    updatedSourceMap[event.source.name] = true
                    _state.value = _state.value.copy(
                        sources = updatedSourceMap
                    )
                }
            }
        }
    }

    private suspend fun updateNewsFeed(sourcesList: List<Source>) {
        _state.value = _state.value.copy(
            isLoading = true
        )

        when (val refreshResponse = useCases.refreshNewsFeed.invoke(sourcesList, lastUpdateTime)) {
            RefreshResponse.Success -> {
                Log.d("TAG__", "SUCCESS refresh response")
                _state.value = _state.value.copy(
                    isError = false,
                )
            }

            is RefreshResponse.Failure -> {
                Log.d("TAG__", "FAILURE: ${refreshResponse.message}")
                _state.value = _state.value.copy(
                    isError = true,
                    errorMessage = refreshResponse.message
                )
            }

            RefreshResponse.NoNetworkConnection -> {}
        }

        _state.value = _state.value.copy(
            isLoading = false
        )

        _scrollFlow.emit(true)
    }
}


