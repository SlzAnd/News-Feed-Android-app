package com.example.newsfeed.domain.model

import androidx.compose.ui.graphics.Color
import com.example.newsfeed.ui.theme.azureRadiance
import com.example.newsfeed.ui.theme.dustyGray
import com.example.newsfeed.ui.theme.tuscany
import com.example.newsfeed.ui.theme.webOrange
import com.example.newsfeed.ui.theme.wedgeWood

enum class Source(
    val simpleName: String,
    val color: Color,
    val baseUrl: String
) {
    NYT(
        simpleName = "NYT",
        color = webOrange,
        baseUrl = "https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml"
    ),
    FOX_NEWS(
        simpleName = "Fox News",
        color = azureRadiance,
        baseUrl = "https://moxie.foxnews.com/google-publisher/latest.xml"
    ),
    THE_GUARDIAN(
        simpleName = "The Guardian",
        color = dustyGray,
        baseUrl = "https://www.theguardian.com/europe/rss"
    ),
    SKY_SPORTS(
        simpleName = "SkySports",
        color = tuscany,
        baseUrl = "https://www.skysports.com/rss/11095"
    ),
    DEV_UA(
        simpleName = "Dev UA",
        color = wedgeWood,
        baseUrl = "https://dev.ua/rss/"
    )
}