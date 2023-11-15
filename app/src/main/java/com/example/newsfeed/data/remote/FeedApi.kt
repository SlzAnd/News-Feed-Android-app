package com.example.newsfeed.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface FeedApi {
    @GET
    suspend fun getFeed(@Url url: String): Result<RssFeed>
}