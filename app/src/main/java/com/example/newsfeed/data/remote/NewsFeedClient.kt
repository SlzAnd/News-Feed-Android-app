package com.example.newsfeed.data.remote

import com.example.newsfeed.domain.model.News
import com.example.newsfeed.domain.model.Source
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import retrofit2.Retrofit
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class NewsFeedClient @Inject constructor(
    private val retrofit: Retrofit
) {

    suspend fun getNewsFromSource(source: Source): Result<RssFeed> {
        return withContext(Dispatchers.IO) {
            val api = retrofit.create(FeedApi::class.java)
            api.getFeed(source.baseUrl)
        }
    }

    fun parseNewsItems(channel: Channel, source: Source): List<News> {
        val items = ArrayList<News>()
        val itemElements: List<Item> = channel.items!!
        val formatter1 = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss 'GMT'")
        val formatter2 = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z")

        for (itemElement in itemElements) {
            val title = itemElement.title?.let { Jsoup.parse(it).text() }

            val link = itemElement.links?.get(0)

            val index = itemElement.imageUrlFromContent?.let {
                if (it.size > 1) {
                    1
                } else {
                    0
                }
            }

            val imageUrl = itemElement.imageUrlFromEnclosure?.imageUrl
                ?: itemElement.imageUrlFromContent?.get(index ?: 0)?.imageUrl
                ?: ""

            val pubDate = try {
                LocalDateTime.parse(
                    itemElement.pubDate,
                    formatter1
                )
            } catch (e: DateTimeParseException) {
                try {
                    LocalDateTime.parse(
                        itemElement.pubDate,
                        formatter2
                    )
                } catch (e: DateTimeParseException) {
                    LocalDateTime.MIN
                }
            }

            val newsItem = News(
                id = link.hashCode(),
                newsSource = source,
                title = title ?: "",
                url = link ?: "",
                imageUrl = imageUrl,
                pubDate = pubDate,
                isBookmarked = false
            )
            items.add(newsItem)
        }
        return items
    }
}