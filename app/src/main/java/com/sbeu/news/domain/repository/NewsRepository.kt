package com.sbeu.news.domain.repository

import com.sbeu.news.domain.entity.Article
import com.sbeu.news.domain.entity.Language
import com.sbeu.news.domain.entity.RefreshConfig
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getAllSubscriptions(): Flow<List<String>>

    suspend fun addSubscription(topic: String)

    suspend fun updateArticlesForTopic(topic: String, language: Language): Boolean

    suspend fun removeSubscription(topic: String)

    suspend fun updateArticlesForAllSubscriptions(language: Language): List<String>

    fun getArticlesByTopics(topics: List<String>): Flow<List<Article>>

    fun startBackgroundRefresh(refreshConfig: RefreshConfig)

    suspend fun clearAllArticles(topics: List<String>)
}