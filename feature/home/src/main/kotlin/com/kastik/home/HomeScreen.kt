package com.kastik.home

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.kastik.data.mappers.toTitleList
import com.kastik.home.components.AnnouncementCard
import com.kastik.home.components.FloatingToolBar
import com.kastik.home.components.TestNewSearchBar

import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class,
)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    navigateToAnnouncement: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    navigateToProfile: () -> Unit
) {

    val authors = viewModel.authors.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val searchBarState = rememberSearchBarState()
    val textFieldState = rememberTextFieldState()
    val lazyItems = viewModel.announcements.collectAsLazyPagingItems()
    val lazyListState = rememberLazyListState()
    val searchScroll = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    //val toolbarScroll = FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection = End)

    LaunchedEffect(lazyListState.firstVisibleItemIndex) {
        Log.d("thequickbrownfox", "${lazyListState.firstVisibleItemIndex}")
    }

    Scaffold(
        topBar = {
            TestNewSearchBar(
                scrollBehavior = searchScroll,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                navigateToSettings = navigateToSettings,
            )
        },
        floatingActionButton = {
            FloatingToolBar(expanded = lazyListState.isScrollingUp(), onFabClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            })
        },
        floatingActionButtonPosition = FabPosition.End,


        ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (lazyItems.loadState.refresh) {
                is LoadState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularWavyProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                        Spacer(Modifier.height(12.dp))
                        Text("Fetching Announcements...")
                    }
                }

                is LoadState.Error -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.Center),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Failed to load. Tap to retry.",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { lazyItems.retry() })
                    }
                }

                else -> Unit
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    //.nestedScroll(toolbarScroll)
                    .nestedScroll(searchScroll.nestedScrollConnection), state = lazyListState
            ) {
                items(
                    count = lazyItems.itemCount,
                    key = lazyItems.itemKey { it.id },
                    contentType = lazyItems.itemContentType { "announcement_card" }) { index ->
                    val item = lazyItems[index]
                    if (item != null) {
                        AnnouncementCard(
                            onClick = { navigateToAnnouncement(item.id) },
                            publisher = item.author.name,
                            title = item.title,
                            categories = remember(item.tags) { item.tags.toTitleList() },
                            date = item.updatedAt,
                            content = remember(item.preview) { item.preview.orEmpty() })
                    }
                }
                when (lazyItems.loadState.append) {
                    is LoadState.Loading -> item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            CircularWavyProgressIndicator(
                                modifier = Modifier.size(32.dp),
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text("Getting next page...")
                        }
                    }

                    is LoadState.Error -> item {
                        Text(
                            "Retry",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable { lazyItems.retry() })

                    }

                    else -> Unit
                }
            }
        }
        ExpandedFullScreenSearchBar(
            state = searchBarState, inputField = {
                SearchBarDefaults.InputField(
                    textFieldState = textFieldState,
                    searchBarState = searchBarState,
                    onSearch = {},
                    placeholder = { Text("Search...") })
            }) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                maxLines = 2
            ) {
                authors.value.forEach {
                    AssistChip(onClick = {}, label = {
                        Text(it.name, style = MaterialTheme.typography.labelLarge)
                    })
                }

            }
        }
    }
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}