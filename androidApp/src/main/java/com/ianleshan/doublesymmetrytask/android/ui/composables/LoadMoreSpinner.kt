package com.ianleshan.doublesymmetrytask.android.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ianleshan.doublesymmetrytask.SessionsViewModel
import com.ianleshan.doublesymmetrytask.UIState
import kotlinx.coroutines.delay


fun LazyGridScope.loadMoreSpinner(
    uiState: UIState,
    viewModel: SessionsViewModel,
) {
    if (uiState.hasNextPage())
        item(
            span = {
                GridItemSpan(2)
            }
        ) {
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                LaunchedEffect(key1 = Unit, block = {
                    delay(300)
                    viewModel.loadNextPage()
                })

                if (uiState.error == null) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White.copy(alpha = .56f),
                        strokeWidth = 2.dp,
                    )
                } else {
                    IconButton(
                        onClick = {
                            viewModel.loadNextPage()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            tint = Color.White,
                            contentDescription = "Something went wrong"
                        )
                    }
                }
            }
        }
}