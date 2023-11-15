package com.example.newsfeed.data

import android.content.Context
import android.util.Log
import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.data.local.NewsDao
import com.example.newsfeed.data.remote.NewsFeedClient
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.RefreshResponse
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.repository.NewsRepository
import com.example.newsfeed.util.isNetworkEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiClient: NewsFeedClient,
    private val newsDao: NewsDao,
    private val context: Context,
    private val dataStore: StoreSettings
) : NewsRepository {

    override suspend fun refreshNews(
        sources: List<Source>,
        lastUpdateTime: LocalDateTime?
    ): RefreshResponse {
        val time: LocalDateTime = lastUpdateTime ?: LocalDateTime.MIN
        var failureMessage = ""
        if (context.isNetworkEnabled()) {
            val deferredResults = coroutineScope {
                async {
                    sources.forEach { source ->
                        val result = async(Dispatchers.IO) {
                            apiClient.getNewsFromSource(source)
                        }
                        result.await().onSuccess { rssFeed ->
                            val newNews = apiClient.parseNewsItems(rssFeed.channel!!, source)
                            val newsToInsert =
                                newNews.filter { it.pubDate.isAfter(time) }
                            if (newsToInsert.isNotEmpty()) {
                                newsDao.saveNews(newsToInsert)
                            }
                        }.onFailure {
                            Log.d("TAG__", "FAILURE ON REPO: ${it.localizedMessage}")
                            failureMessage = it.localizedMessage ?: "Network request failed!"
                        }
                    }
                }
            }
            deferredResults.await()

            return if (failureMessage.isNotEmpty()) {
                RefreshResponse.Failure(failureMessage)
            } else {
                dataStore.setLastUpdateTime(LocalDateTime.now().toString())
                RefreshResponse.Success
            }
        } else return RefreshResponse.NoNetworkConnection
    }

    override suspend fun getNewsBySource(sources: List<Source>): Flow<List<News>> {
        if (sources.isEmpty()) return flowOf(emptyList())
        return coroutineScope {
            combine(sources.map { newsDao.getNewsBySource(it) }) { array ->
                array.flatMap { it }
            }
        }
    }

    override fun getAllNews(): Flow<List<News>> {
        return newsDao.getAllNews()
    }

    override suspend fun updateNews(news: News) {
        newsDao.updateNews(news)
    }

    override suspend fun getNewsById(id: Int): News? {
        return newsDao.getNewsById(id)
    }
}