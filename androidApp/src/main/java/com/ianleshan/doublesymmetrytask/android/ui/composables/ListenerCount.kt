package com.ianleshan.doublesymmetrytask.android.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ianleshan.doublesymmetrytask.android.R

@Composable
fun ListenerCount(
    modifier: Modifier = Modifier,
    count: Int,
) {
    Row(
        modifier = modifier
            .background(
                color = Color.White.copy(alpha = .75f),
                shape = RoundedCornerShape(20.dp)
            )
            .defaultMinSize(minWidth = 37.dp)
            .height(20.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_listener_icon),
            contentDescription = null)

        Spacer(Modifier.width(2.dp))

        Text(text = "$count", color = Color.Black, fontSize = 11.sp)
    }
}