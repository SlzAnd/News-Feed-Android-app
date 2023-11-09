package com.example.newsfeed.data.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface FeedApi {
    @GET
    fun getFeed(@Url relativeUrl: String): Call<RssFeed>

    class ApiRequestException(message: String) : Exception(message)
}