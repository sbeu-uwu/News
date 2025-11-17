package com.sbeu.news.domain.repository

import com.sbeu.news.domain.entity.Article
import com.sbeu.news.domain.entity.RefreshConfig
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun addSubscription(topic: String)

    suspend fun updateArticlesForTopic(topic: String)

    suspend fun removeSubscription(topic: String)

    suspend fun updateArticlesForAllSubscriptions(): List<String>

    fun getArticlesByTopics(topics: List<String>): Flow<List<Article>>

    fun startBackgroundRefresh(refreshConfig: RefreshConfig)

    suspend fun clearAllArticles(topics: List<String>)
}