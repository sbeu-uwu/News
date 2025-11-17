package com.sbeu.news.domain.repository

import com.sbeu.news.domain.entity.Language
import com.sbeu.news.domain.entity.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getSettings(): Flow<Settings>

    suspend fun updateLanguage(language: Language)

    suspend fun updateInterval(minutes: Int)

    suspend fun updateNotificationsStatus(enabled: Boolean)

    suspend fun updateWifiOnlyStatus(wifiOnly: Boolean)
}