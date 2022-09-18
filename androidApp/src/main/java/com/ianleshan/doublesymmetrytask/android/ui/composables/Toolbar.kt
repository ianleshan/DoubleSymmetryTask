package com.ianleshan.doublesymmetrytask.android.ui.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.ianleshan.doublesymmetrytask.SessionsViewModel
import com.ianleshan.doublesymmetrytask.UIState
import com.ianleshan.doublesymmetrytask.android.ui.theme.topBarColor
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScope
import me.onebone.toolbar.CollapsingToolbarState
import me.onebone.toolbar.ExperimentalToolbarApi

@OptIn(ExperimentalToolbarApi::class)
@Composable
fun CollapsingToolbarScope.Toolbar(
    toolbarState: CollapsingToolbarState,
    listState: LazyGridState,
    uiState: UIState,
    viewModel: SessionsViewModel,
) {
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .animateContentSize(
                animationSpec = tween(50),
                finishedListener = { _, _ ->
                    scope.launch {
                        toolbarState.expand()
                    }
                }
            )
            .clickable {
                scope.launch {
                    launch {
                        listState.animateScrollToItem(0)
                    }
                    toolbarState.expand()
                }
            }
            .background(topBarColor)
            .fillMaxWidth()
            .height(if (uiState.searchTerm.isEmpty()) 192.dp else 56.dp)
            .parallax(.5f)
    ) {
        CustomTextField(
            value = uiState.searchTerm, onValueChange = {
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
                when {
                    uiState.loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White.copy(alpha = .56f),
                            strokeWidth = 2.dp,
                        )
                    }
                    uiState.error != null && uiState.searchTerm.isNotEmpty() -> {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                    }
                    else -> {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
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
            toolbarState.progress > 0
        }
    }

    val textSize = (18 + (34 - 18) * toolbarState.progress).sp

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
                    bottom = lerp(10.dp, 56.dp, toolbarState.progress),
                    top = 32.dp,
                    start = lerp(0.dp, 16.dp, toolbarState.progress),
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
                    toolbarState.expand()
                }
            })
        }
    }
}