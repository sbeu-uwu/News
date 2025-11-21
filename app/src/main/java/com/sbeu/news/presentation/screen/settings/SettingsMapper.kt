package com.sbeu.news.presentation.screen.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sbeu.news.R
import com.sbeu.news.domain.entity.Interval
import com.sbeu.news.domain.entity.Language

@Composable
fun Language.toReadableFormat(): String {
    return when (this) {
        Language.ENGLISH -> stringResource(R.string.english)
        Language.RUSSIAN -> stringResource(R.string.russian)
        Language.GERMAN -> stringResource(R.string.francais)
        Language.FRENCH -> stringResource(R.string.deutsch)
    }
}

@Composable
fun Interval.toReadableFormat(): String {
    return when (this) {
        Interval.MIN_15 -> "15 minutes"
        Interval.MIN_30 -> "30 minutes"
        Interval.HOUR_1 -> "1 hour"
        Interval.HOUR_2 -> "2 hour"
        Interval.HOUR_4 -> "4 hour"
        Interval.HOUR_8 -> "8 hour"
        Interval.HOUR_12 -> "12 hour"
        Interval.HOUR_24 -> "24 hour"
    }
}