package com.sbeu.news.data.mapper

import com.sbeu.news.domain.entity.RefreshConfig
import com.sbeu.news.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(language, interval, isWifiOnly)
}