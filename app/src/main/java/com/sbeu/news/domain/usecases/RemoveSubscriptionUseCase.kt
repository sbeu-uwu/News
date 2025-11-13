package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.NewsRepository
import javax.inject.Inject

class RemoveSubscriptionUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke(topic: String) {
        newsRepository.removeSubscription(topic = topic)
    }
}