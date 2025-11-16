package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.NewsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic = topic)
        CoroutineScope(currentCoroutineContext()).launch {
            newsRepository.updateArticlesForTopic(topic)
        }
    }
}