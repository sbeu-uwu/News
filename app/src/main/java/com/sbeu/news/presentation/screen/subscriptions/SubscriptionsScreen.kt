@file:OptIn(ExperimentalMaterial3Api::class)

package com.sbeu.news.presentation.screen.subscriptions

import android.content.Intent
import android.widget.Space
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.sbeu.news.R
import com.sbeu.news.domain.entity.Article
import com.sbeu.news.presentation.utils.formatDate

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit,
    viewModel: SubscriptionsViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SubscriptionsTopBar(
                onRefreshDataClick = {
                    viewModel.processCommand(SubscriptionsCommand.RefreshData)
                },
                onClearArticlesClick = {
                    viewModel.processCommand(SubscriptionsCommand.ClearArticles)
                },
                onSettingsClick = onNavigateToSettings
            )
        }
    ) { innerPadding ->
        val state by viewModel.state.collectAsState()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Subscriptions(
                    subscriptions = state.subscriptions,
                    query = state.query,
                    isSubscribeButtonEnabled = state.subscribeButtonEnabled,
                    onQueryChanged = {
                        viewModel.processCommand(SubscriptionsCommand.InputTopic(it))
                    },
                    onSubscribeButtonClick = {
                        viewModel.processCommand(SubscriptionsCommand.ClickSubscribe)
                    },
                    onTopicClick = {
                        viewModel.processCommand(SubscriptionsCommand.ToggleTopicSelection(it))
                    },
                    onDelete = {
                        viewModel.processCommand(SubscriptionsCommand.RemoveSubscription(it))
                    }
                )
            }
            if (state.articles.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.articles, state.articles.size),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                items(
                    items = state.articles,
                    key = { it.url }
                ) {
                    ArticleCard(article = it)
                }
            } else if (state.subscriptions.isNotEmpty()) {
                item {
                    HorizontalDivider()
                }
                item {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_articles_for_selected_subscriptions),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun SubscriptionsTopBar(
    modifier: Modifier = Modifier,
    onRefreshDataClick: () -> Unit,
    onClearArticlesClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.news),
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onClearArticlesClick()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(R.string.refresh_articles)
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onRefreshDataClick()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.clear_articles)
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        onSettingsClick()
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Settings,
                contentDescription = stringResource(R.string.settings)
            )
        }
    )
}

@Composable
private fun SubscriptionChip(
    modifier: Modifier = Modifier,
    topic: String,
    isSelected: Boolean,
    onSubscriptionClick: (String) -> Unit,
    onDelete: (String) -> Unit
) {
    FilterChip(
        modifier = modifier,
        selected = isSelected,
        onClick = {
            onSubscriptionClick(topic)
        },
        label = {
            Text(text = topic)
        },
        trailingIcon = {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .clickable {
                        onDelete(topic)
                    },
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.remove_subscription)
            )
        }
    )
}

@Composable
private fun Subscriptions(
    modifier: Modifier = Modifier,
    subscriptions: Map<String, Boolean>,
    query: String,
    isSubscribeButtonEnabled: Boolean,
    onQueryChanged: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onDelete: (String) -> Unit,
    onSubscribeButtonClick: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
                value = query,
                onValueChange = onQueryChanged,
                label = {
                    Text(text = "Search")
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(60.dp)
                    .padding(top = 4.dp),
                onClick = {
                    onSubscribeButtonClick()
                },
                enabled = isSubscribeButtonEnabled,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_subscription)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = stringResource(R.string.add))
            }
        }

        Spacer(Modifier.height(16.dp))

        if (subscriptions.isNotEmpty()) {
            Text(
                text = stringResource(R.string.subscriptions, subscriptions.size),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = subscriptions.toList(),
                    key = { it.first }
                ) { (topic, isSelected) ->
                    SubscriptionChip(
                        topic = topic,
                        isSelected = isSelected,
                        onSubscriptionClick = onTopicClick,
                        onDelete = onDelete
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.no_subscriptions),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ArticleCard(
    modifier: Modifier = Modifier,
    article: Article
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        article.imageUrl?.let { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = stringResource(R.string.image_for_article, article),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
            )
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = article.title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        if (article.description.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = article.description,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val context = LocalContext.current
            Text(
                text = "${article.sourceName} - ${article.publishedAt.formatDate()}",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 12.sp
            )
            Spacer(Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, article.url.toUri())
                        context.startActivity(intent)
                    }
                    .padding(8.dp),
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = stringResource(R.string.open_full_article, article)
            )
            Spacer(Modifier.width(8.dp))
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "${article.title} \n\n${article.url}")
                        }
                        context.startActivity(intent)
                    }
                    .padding(8.dp),
                imageVector = Icons.Default.Share,
                contentDescription = stringResource(R.string.share_article, article)
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}



























