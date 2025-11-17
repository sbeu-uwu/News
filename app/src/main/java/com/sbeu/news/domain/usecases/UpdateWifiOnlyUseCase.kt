package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateWifiOnlyUseCase @Inject constructor(
    val repository: SettingsRepository
) {

    suspend operator fun invoke(wifiOnly: Boolean) {
        repository.updateWifiOnlyStatus(wifiOnly)
    }
}