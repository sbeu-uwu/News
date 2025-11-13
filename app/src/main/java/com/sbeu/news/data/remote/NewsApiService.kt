package com.sbeu.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=bde3efadc0994ad0a18dd5df6415c545")
    suspend fun loadArticles(
        @Query("q") topic: String,
//        @Query("language") language: String
    ): NewsResponseDto
}