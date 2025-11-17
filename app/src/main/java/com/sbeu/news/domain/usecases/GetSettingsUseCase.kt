package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor (
    val repository: SettingsRepository
) {

    operator fun invoke() = repository.getSettings()
}