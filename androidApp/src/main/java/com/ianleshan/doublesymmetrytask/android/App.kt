package com.ianleshan.doublesymmetrytask.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun App() {
    val viewModel: SessionsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState(initial = UIState())

    LaunchedEffect(key1 = Unit, block = {
        viewModel.onCreate()
    })

    val state = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .background(topBarColor)
                    .fillMaxWidth()
                    .height(192.dp)
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
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
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
                    state.toolbarState.progress > 0
                }
            }

            val textSize = (18 + (34 - 18) * state.toolbarState.progress).sp

            Text(
                text = uiState.title,
                modifier = Modifier
                    .road(Alignment.BottomCenter, Alignment.BottomStart)
                    .padding(
                        bottom = lerp(10.dp, 56.dp, state.toolbarState.progress),
                        top = 32.dp,
                        start = lerp(0.dp, 16.dp, state.toolbarState.progress),
                    ),
                color = Color.White,
                fontSize = textSize,
                fontWeight = if (useHeavyFont) FontWeight.Bold else FontWeight.Normal
            )

        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            content = {
                items(uiState.getList()) { session ->
                    SessionCard(
                        modifier = Modifier.padding(8.dp),
                        session,
                    )
                }

            }
        )
    }

}