package com.example.newsfeed.presentation.article

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.use_case.NewsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val useCases: NewsUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _currentNews: News? = null

    private val _state = MutableStateFlow(ArticleState())
    val state: StateFlow<ArticleState> = _state

    init {
        savedStateHandle.get<Int>("newsId")?.let { newsId ->
            viewModelScope.launch(Dispatchers.IO) {
                useCases.getNewsById.invoke(newsId)?.also {
                    _currentNews = it

                    _state.value = _state.value.copy(
                        url = it.url,
                        isBookmarked = it.isBookmarked
                    )
                }
            }
        }
    }

    fun onBookmarkClick() {
        val isBookmarkValue = _currentNews!!.isBookmarked
        val updatedNewsItem = _currentNews!!.copy(
            isBookmarked = !isBookmarkValue
        )

        _state.value = _state.value.copy(
            isBookmarked = updatedNewsItem.isBookmarked
        )

        viewModelScope.launch(Dispatchers.IO) {
            useCases.updateNewsInCache.invoke(updatedNewsItem)
        }
    }
}