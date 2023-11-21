package com.example.newsfeed.ui.news_feed

import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.RefreshResponse
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.use_case.NewsUseCases
import com.example.newsfeed.util.ConnectivityObserver
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class NewsViewModelTest {

    private val useCases: NewsUseCases = mockk()
    private val dataStore: StoreSettings = mockk()
    private val observer: ConnectivityObserver = mockk()

    private lateinit var viewModel: NewsViewModel

    private val news1 = News(
        id = "https:url1.com.ua".hashCode(),
        url = "https:url1.com.ua",
        newsSource = Source.FOX_NEWS,
        title = "News article for test",
        imageUrl = "image test url",
        pubDate = LocalDateTime.now(),
        isBookmarked = false
    )

    private val news2 = News(
        id = "https:url2.com.ua".hashCode(),
        url = "https:url2.com.ua",
        newsSource = Source.FOX_NEWS,
        title = "News article for test",
        imageUrl = "image test url",
        pubDate = LocalDateTime.now(),
        isBookmarked = false
    )
    private val news3 = News(
        id = "https:url3.com.ua".hashCode(),
        url = "https:url3.com.ua",
        newsSource = Source.THE_GUARDIAN,
        title = "News article for test",
        imageUrl = "image test url",
        pubDate = LocalDateTime.now(),
        isBookmarked = false
    )

    private val testFlow = flowOf(listOf(news1, news2, news3))
    private val sources = Source.entries.toList()
    private val testErrorMessage = "API test request failed"
    private val time = LocalDateTime.MIN

    @Before
    fun setUp() {
        coEvery { useCases.getAllNews.invoke() } returns  testFlow
        every { dataStore.getLastUpdateTime() } returns time.toString()
        coEvery { useCases.getNewsFromSource(listOf(Source.THE_GUARDIAN)) } returns flowOf(listOf(news3))
    }

    @After
    fun tearDown() {
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Failure refresh response should change UI state to error`() = runTest {
        coEvery { useCases.refreshNewsFeed.invoke(sources, time) } returns RefreshResponse.Failure(testErrorMessage)

        viewModel = NewsViewModel(useCases, dataStore, observer)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()

            assertThat(viewModel.state.value.isError).isFalse() // initial value
            assertThat(viewModel.state.value.errorMessage).isEqualTo("Error message") // initial value

            viewModel.onEvent(NewsScreenEvent.UpdateAllNewsFeed)

            delay(500)

            assertThat(viewModel.state.value.isError).isTrue()
            assertThat(viewModel.state.value.errorMessage).isEqualTo(testErrorMessage)
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Adding source should add news from this source`() = runTest {
        coEvery { useCases.refreshNewsFeed.invoke(sources, time) } returns RefreshResponse.Success

        viewModel = NewsViewModel(useCases, dataStore, observer)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.state.collect()

            assertThat(viewModel.state.value.news.size).isEqualTo(0) // initial value is empty list

            viewModel.onEvent(NewsScreenEvent.AddRemoveNewsSource(Source.THE_GUARDIAN)) // add source

            delay(500)

            assertThat(viewModel.state.value.news.size).isEqualTo(1)
        }
    }
}