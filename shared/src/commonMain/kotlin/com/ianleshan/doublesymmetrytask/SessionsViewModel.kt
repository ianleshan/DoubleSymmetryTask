package com.ianleshan.doublesymmetrytask

import com.doublesymmetry.viewmodel.ViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class SessionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState
    private val searchEntry = MutableSharedFlow<String>()
    private val api = ApiRepository()

    @OptIn(FlowPreview::class)
    fun onCreate() {
        viewModelScope.launch {
            searchEntry
                .debounce(500)
                .collect { searchTerm ->
                    try {
                        val sessions = api.searchSessions(searchTerm)
                        _uiState.emit(
                            _uiState.value.copy(
                                searchSessions = sessions,
                                error = null,
                                loading = false,
                            )
                        )
                    } catch (e: Exception) {
                        _uiState.emit(
                            _uiState.value.copy(
                                error = e,
                                searchSessions = emptyList(),
                                loading = false,
                            )
                        )
                    }
                }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            try {
                val sessions = api.getSessions()
                val newState = _uiState.value.copy(
                    discoverSessions = _uiState.value.discoverSessions.plus(sessions),
                    currentPage = _uiState.value.currentPage + 1,
                    error = null,
                    loading = false,
                )
                _uiState.emit(newState)
            } catch (e: Exception) {
                _uiState.emit(
                    _uiState.value.copy(
                        error = e,
                        loading = false,
                    )
                )
            }
        }
    }

    fun search(newEntry: String) {
        if (newEntry.isNotEmpty()) {
            viewModelScope.launch {
                _uiState.emit(
                    _uiState.value.copy(
                        searchTerm = newEntry,
                        loading = true,
                    )
                )
                searchEntry.emit(newEntry)
            }
        } else {
            viewModelScope.launch {
                _uiState.emit(
                    UIState()
                )
            }
        }
    }
}