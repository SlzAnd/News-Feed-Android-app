package com.example.newsfeed.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class NewsDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var dao: NewsDao

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


    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test_saveNewsAndGetNewsById() {
        val newsList = listOf(news1, news2, news3)
        runTest {
            dao.saveNews(newsList)
            assertThat(dao.getNewsById(news1.id)).isEqualTo(news1)
            assertThat(dao.getNewsById(news2.id)).isEqualTo(news2)
            assertThat(dao.getNewsById(news3.id)).isEqualTo(news3)
        }
    }

    @Test
    fun test_saveNewsAndGetBySource() {
        val newsList = listOf(news1, news2, news3)

        runTest {
            dao.saveNews(newsList)

            assertThat(dao.getNewsBySource(Source.FOX_NEWS).firstOrNull()).isNotNull()
            assertThat(dao.getNewsBySource(Source.FOX_NEWS).first().size).isEqualTo(2)

            assertThat(dao.getNewsBySource(Source.DEV_UA).firstOrNull()).isNotNull()
            assertThat(dao.getNewsBySource(Source.DEV_UA).first().size).isEqualTo(0)
        }
    }

    @Test
    fun test_updateNews() {
        val newsList = listOf(news1, news2, news3)
        val updatedTitle = "Updated title"
        val updatedNews3 = News(
            id = "https:url3.com.ua".hashCode(),
            url = "https:url3.com.ua",
            newsSource = Source.THE_GUARDIAN,
            title = updatedTitle,
            imageUrl = "image test url",
            pubDate = LocalDateTime.now(),
            isBookmarked = true
        )

        runTest {
            dao.saveNews(newsList)

            dao.updateNews(updatedNews3)

            assertThat(dao.getNewsById(news3.id)?.isBookmarked).isTrue()
            assertThat(dao.getNewsById(news3.id)?.title).isEqualTo(updatedTitle)
        }
    }

    @Test
    fun test_deleteNews() {
        val newsList = listOf(news1, news2, news3)

        runTest {
            dao.saveNews(newsList)
            assertThat(dao.getAllNews().first().size).isEqualTo(3)

            dao.deleteAll()
            assertThat(dao.getAllNews().first().size).isEqualTo(0)
        }
    }
}