package com.sbeu.news.domain.usecases

import com.sbeu.news.domain.entity.Language
import com.sbeu.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor (
    val repository: SettingsRepository
) {

    suspend operator fun invoke(language: Language) {
        repository.updateLanguage(language)
    }
}