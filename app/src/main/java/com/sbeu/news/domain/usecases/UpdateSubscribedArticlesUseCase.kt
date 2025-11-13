package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {

    suspend operator fun invoke() {
        newsRepository.updateArticlesForAllSubscriptions()
    }
}