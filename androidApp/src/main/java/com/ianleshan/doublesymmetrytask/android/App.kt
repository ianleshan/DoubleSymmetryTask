package com.ianleshan.doublesymmetrytask.android

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ianleshan.doublesymmetrytask.SessionsViewModel
import com.ianleshan.doublesymmetrytask.UIState
import com.ianleshan.doublesymmetrytask.android.ui.composables.ErrorStateUI
import com.ianleshan.doublesymmetrytask.android.ui.composables.SessionCard
import com.ianleshan.doublesymmetrytask.android.ui.composables.Toolbar
import com.ianleshan.doublesymmetrytask.android.ui.composables.loadMoreSpinner
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Composable
fun App() {
    val viewModel: SessionsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState(
        initial = UIState()
    )

    LaunchedEffect(key1 = Unit, block = {
        viewModel.onCreate()
    })

    val listState = rememberLazyGridState()
    val collapseState = rememberCollapsingToolbarScaffoldState()
    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapseState,
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Toolbar(
                toolbarState = collapseState.toolbarState,
                listState = listState,
                uiState = uiState,
                viewModel = viewModel,
            )
        }
    ) {
        if (uiState.error == null || uiState.getList()
                .isNotEmpty() || uiState.searchTerm.isNotEmpty()
        ) {
            LazyVerticalGrid(
                modifier = Modifier.focusable(),
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

                    loadMoreSpinner(
                        uiState = uiState,
                        viewModel = viewModel,
                    )

                }
            )
        } else {
            ErrorStateUI()
        }
    }


    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = Unit, block = {
        snapshotFlow { listState.isScrollInProgress }
            .collect {
                focusManager.clearFocus()
            }
    })

}