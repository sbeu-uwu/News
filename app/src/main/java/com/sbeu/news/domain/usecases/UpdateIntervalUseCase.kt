package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.entity.Interval
import com.sbeu.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    val repository: SettingsRepository,

    ) {

    suspend operator fun invoke(interval: Interval) {
        repository.updateInterval(interval.minutes)
    }
}