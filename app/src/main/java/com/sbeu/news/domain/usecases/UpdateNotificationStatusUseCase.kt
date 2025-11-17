package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationStatusUseCase @Inject constructor(
    val repository: SettingsRepository
) {

    suspend operator fun invoke(enabled: Boolean) {
        repository.updateNotificationsStatus(enabled)
    }
}