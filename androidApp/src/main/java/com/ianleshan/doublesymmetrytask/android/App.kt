package com.ianleshan.doublesymmetrytask.android

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ianleshan.doublesymmetrytask.SessionsViewModel
import com.ianleshan.doublesymmetrytask.UIState
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun App() {
    val viewModel: SessionsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState(initial = UIState())

    LaunchedEffect(key1 = Unit, block = {
        viewModel.onCreate()
    })

    val scope = rememberCoroutineScope()
    val listState = rememberLazyGridState()
    val collapseState = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapseState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(50),
                        finishedListener = { _, _ ->
                            scope.launch {
                                collapseState.toolbarState.expand()
                            }
                        }
                    )
                    .clickable {
                        scope.launch {
                            launch {
                                listState.animateScrollToItem(0)
                            }
                            collapseState.toolbarState.expand()
                        }
                    }
                    .background(topBarColor)
                    .fillMaxWidth()
                    .height(if (uiState.searchTerm.isEmpty()) 192.dp else 56.dp)
                    .parallax(.5f)
            ) {
                CustomTextField(
                    value = uiState.searchTerm, onValueChange = {
                        scope.launch {
                            collapseState.toolbarState.expand()
                        }
                        viewModel.search(it)
                    },
                    modifier = Modifier
                        .height(56.dp)
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    placeholder = {
                        Text(text = "Search")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    maxLines = 1,
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(0x3D767680),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )
            }

            val useHeavyFont by remember {
                derivedStateOf {
                    collapseState.toolbarState.progress > 0
                }
            }

            val textSize = (18 + (34 - 18) * collapseState.toolbarState.progress).sp

            if (uiState.searchTerm.isEmpty()) {
                var showTitle by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = Unit, block = {
                    delay(100)
                    showTitle = true
                })
                AnimatedVisibility(
                    modifier = Modifier
                        .road(Alignment.BottomCenter, Alignment.BottomStart)
                        .padding(
                            bottom = lerp(10.dp, 56.dp, collapseState.toolbarState.progress),
                            top = 32.dp,
                            start = lerp(0.dp, 16.dp, collapseState.toolbarState.progress),
                        ),
                    visible = showTitle,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut(animationSpec = snap()),
                ) {
                    Text(
                        text = uiState.title,
                        color = Color.White,
                        fontSize = textSize,
                        fontWeight = if (useHeavyFont) FontWeight.Bold else FontWeight.Normal
                    )
                    LaunchedEffect(key1 = Unit, block = {
                        scope.launch {
                            awaitFrame()
                            collapseState.toolbarState.expand()
                        }
                    })
                }
            }

        }
    ) {
        if (uiState.error == null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = listState,
                contentPadding = PaddingValues(8.dp),
                content = {
                    items(uiState.getList()) { session ->
                        SessionCard(
                            modifier = Modifier.padding(8.dp),
                            session,
                        )
                    }

                    if (uiState.hasNextPage())
                        item(
                            span = {
                                GridItemSpan(2)
                            }
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                LaunchedEffect(key1 = Unit, block = {
                                    delay(300)
                                    viewModel.loadNextPage()
                                })
                                Box(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .background(
                                            color = Color.Magenta,
                                            shape = CircleShape
                                        )
                                        .size(20.dp)
                                )
                            }
                        }

                }
            )
        } else {
            Text(text = "${uiState.error?.localizedMessage}")
        }
    }

}