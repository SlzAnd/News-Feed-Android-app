package com.example.newsfeed.data

import android.content.Context
import com.example.newsfeed.data.datastore.StoreSettings
import com.example.newsfeed.data.local.NewsDao
import com.example.newsfeed.data.remote.NewsFeedClient
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import com.example.newsfeed.domain.repository.NewsRepository
import com.example.newsfeed.util.isNetworkEnabled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDateTime
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val apiClient: NewsFeedClient,
    private val newsDao: NewsDao,
    private val context: Context,
    private val dataStore: StoreSettings
) : NewsRepository {

    override suspend fun refreshNews(sources: List<Source>, lastUpdateTime: LocalDateTime?) {

        if (context.isNetworkEnabled()) {
            val deferredResults = coroutineScope {
                async {
                    sources.forEach { source ->
                        val newNews = async(Dispatchers.IO) {
                            apiClient.getNewsFromSource(source)
                        }
                        if (lastUpdateTime != null) {
                            val newsToInsert =
                                newNews.await().filter { it.pubDate.isAfter(lastUpdateTime) }
                            if (newsToInsert.isNotEmpty()) {
                                newsDao.saveNews(newsToInsert)
                            }
                        } else {
                            newsDao.saveNews(newNews.await())
                        }
                    }
                }
            }
            if (deferredResults.isCompleted) {
                dataStore.setLastUpdateTime(LocalDateTime.now().toString())
            }
        }
    }

    override suspend fun getNews(sources: List<Source>): Flow<List<News>> {
        return coroutineScope {
            combine(sources.map { newsDao.getNewsBySource(it) }) { array ->
                array.flatMap { it }
            }
        }
    }

    override suspend fun updateNews(news: News) {
        newsDao.updateNews(news)
    }

    override suspend fun getNewsById(id: Int): News? {
        return newsDao.getNewsById(id)
    }
}