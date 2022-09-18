package com.ianleshan.doublesymmetrytask.android.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.ianleshan.doublesymmetrytask.models.Session

@Composable
fun SessionCard(
    modifier: Modifier = Modifier,
    session: Session,
) {

    Box(modifier = modifier
        .clip(RoundedCornerShape(10.dp))
        .fillMaxWidth()
        .aspectRatio(1f)
    ) {
        AsyncImage(
            model = session.currentTrack.art,
            contentDescription = session.name,
            modifier = Modifier.fillMaxSize()
        )

        Box(modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = .01f),
                        Color.Black.copy(alpha = .8f),
                    )
                )
            )
            .fillMaxSize()
        )

        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(text = session.name, color = Color.White, fontSize = 16.sp)
            Text(text = session.getGenres(),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White)
        }

        ListenerCount(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.TopStart),
            count = session.listenerCount
        )

    }

}