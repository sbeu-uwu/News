package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.NewsRepository
import javax.inject.Inject

class AddSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.addSubscription(topic = topic)
        newsRepository.updateArticlesForTopic(topic)
    }
}