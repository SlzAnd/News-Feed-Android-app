package com.example.newsfeed.presentation.news_feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.data.remote.FeedApi
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.use_case.NewsUseCases
import com.example.newsfeed.util.ConnectivityObserver
import com.example.newsfeed.util.ConnectivityObserver.Status.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val dataStore: StoreSettings,
    val connectivityObserver: ConnectivityObserver,
) : ViewModel() {

    private var _sourceList = Source.entries.toList()
    private val _sources = MutableStateFlow(_sourceList)
    private var _state: MutableStateFlow<NewsState> = MutableStateFlow(NewsState())
    private var _lastUpdateTime: LocalDateTime? = null

    private val _newsList = _sources
        .flatMapLatest { sourceList ->
            useCases.getNewsFromSource.invoke(sourceList)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state: StateFlow<NewsState> = combine(_state, _newsList) { state, newsList ->
        state.copy(
            news = newsList.sortedByDescending(News::pubDate),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NewsState())


    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.getLastUpdateTime().collect { timeString ->
                _lastUpdateTime = LocalDateTime.parse(timeString)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            updateNewsFeed()
        }
    }

    fun onEvent(event: NewsScreenEvent) {
        when (event) {
            is NewsScreenEvent.AddToBookmark -> {
                val updatedNewsItem = event.news.copy(
                    isBookmarked = event.isBookmarked
                )

                viewModelScope.launch(Dispatchers.IO) {
                    async { useCases.updateNewsInCache.invoke(updatedNewsItem) }.await()
                    useCases.getNewsFromSource(_sourceList).collectLatest { updatedList ->
                        _state.value = _state.value.copy(
                            news = updatedList
                        )
                    }
                }
            }

            NewsScreenEvent.UpdateNewsFeed -> {
                viewModelScope.launch(Dispatchers.IO) {
                    updateNewsFeed()
                }
            }
        }
    }

    private suspend fun updateNewsFeed() {
        _state.value = _state.value.copy(
            isLoading = true
        )

        try {
            useCases.refreshNewsFeed.invoke(_sourceList, _lastUpdateTime)
            _state.value = _state.value.copy(
                isError = false,
            )
        } catch (e: FeedApi.ApiRequestException) {
            _state.value = _state.value.copy(
                isError = true,
                errorMessage = e.message ?: "API request error"
            )
        }

        _state.value = _state.value.copy(
            isLoading = false
        )
    }
}


