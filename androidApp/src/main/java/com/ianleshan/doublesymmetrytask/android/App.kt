package com.ianleshan.doublesymmetrytask.android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianleshan.doublesymmetrytask.Greeting
import com.ianleshan.doublesymmetrytask.Session
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun App() {

    var sessions: List<Session> by remember {
        mutableStateOf(emptyList())
    }

    LaunchedEffect(key1 = Unit, block = {

        val greeting = Greeting()
        sessions = greeting.getHtml()

    })

    val state = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp

            Box(
                modifier = Modifier
                    .background(topBarColor)
                    .fillMaxWidth()
                    .height(192.dp)
                    .pin()
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(topBarColor)
                        .height(56.dp)
                ) {
                    Box(modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .background(
                            color = Color(0x3D767680),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .fillMaxWidth()
                        .height(36.dp))
                }
            }

            Text(
                text = "Discover",
                modifier = Modifier
                    .road(Alignment.BottomCenter, Alignment.BottomStart)
                    .padding(
                        bottom = lerp(56.dp, 10.dp, 1f - state.toolbarState.progress),
                        top = 32.dp,
                        start = 16.dp
                    ),
                color = Color.White,
                fontSize = textSize
            )

        }
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            content = {
                items(sessions) { session ->
                    Box(modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .fillMaxWidth()
                        .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = session.currentTrack.art,
                            contentDescription = session.name,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

            }
        )
    }

}