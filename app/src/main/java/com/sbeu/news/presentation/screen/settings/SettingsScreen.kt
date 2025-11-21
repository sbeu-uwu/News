package com.sbeu.news.presentation.screen.settings

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.sbeu.news.R

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            viewModel.processCommand(SettingsCommand.SetNotificationsEnabled(it))
        }
    )
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SettingsTopBar(
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        val state = viewModel.state.collectAsState()

        when(val currentState = state.value) {
            is SettingsState.Configuration -> {
                Column(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    SettingsCard(
                        title = stringResource(R.string.search_language),
                        subtitle = stringResource(R.string.select_language_for_news_search),
                        content = {
                            SettingsDropdown(
                                items = currentState.languages,
                                selectedItem = currentState.language,
                                onItemSelected = {
                                    viewModel.processCommand(SettingsCommand.SelectLanguage(it))
                                },
                                itemAsString = {
                                    it.toReadableFormat()
                                }
                            )
                        },
                    )
                    SettingsCard(
                        title = stringResource(R.string.update_interval),
                        subtitle = stringResource(R.string.how_often_to_update_news),
                        content = {
                            SettingsDropdown(
                                items = currentState.intervals,
                                selectedItem = currentState.interval,
                                onItemSelected = {
                                    viewModel.processCommand(SettingsCommand.SelectInterval(it))
                                },
                                itemAsString = {
                                    it.toReadableFormat()
                                }
                            )
                        }
                    )
                    SettingsCard(
                        title = stringResource(R.string.notifications),
                        subtitle = stringResource(R.string.show_notifications_about_new_articles),
                    ) {
                        Switch(
                            checked = currentState.notificationsEnabled,
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                        permissionLauncher.launch(POST_NOTIFICATIONS)
                                    }
                                } else {
                                    viewModel.processCommand(SettingsCommand.SetNotificationsEnabled(false))
                                }
                            }
                        )
                    }
                    SettingsCard(
                        title = stringResource(R.string.update_only_via_wi_fi),
                        subtitle = stringResource(R.string.save_mobile_data)
                    ) {
                        Switch(
                            checked = currentState.wifiOnly,
                            onCheckedChange = { enabled ->
                                viewModel.processCommand(SettingsCommand.SetWifiOnly(enabled))
                            }
                        )
                    }
                }
            }
            SettingsState.Initial -> {  }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.settings))
        },
        navigationIcon = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onBackClick()
                    }
                    .padding(16.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.navigate_back_to_main_screen)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier,
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier,
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))

            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> SettingsDropdown(
    modifier: Modifier = Modifier,
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemAsString: @Composable (T) -> String
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = itemAsString(selectedItem),
            readOnly = true,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(itemAsString(item))
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}