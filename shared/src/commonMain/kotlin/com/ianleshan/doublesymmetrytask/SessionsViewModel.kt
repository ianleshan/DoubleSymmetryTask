package com.ianleshan.doublesymmetrytask

import com.doublesymmetry.viewmodel.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SessionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState
    private val searchEntry = MutableSharedFlow<String>()

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }

    private suspend fun getSessions(): List<Session> {
        return client
            .get("https://www.mocky.io/v2/5df79a3a320000f0612e0115")
            .body<Response>()
            .data
            .sessions
    }

    private suspend fun searchSessions(
        searchTerm: String // Not used
    ): List<Session> {
        return client
            .get("https://www.mocky.io/v2/5df79b1f320000f4612e011e")
            .body<Response>()
            .data
            .sessions
            .shuffled()
    }

    @OptIn(FlowPreview::class)
    fun onCreate() {
        viewModelScope.launch {
            val sessions = getSessions()
            val newState = _uiState.value.copy(
                discoverSessions = sessions,
            )
            _uiState.emit(newState)
        }

        viewModelScope.launch {
            searchEntry
                .debounce(1000)
                .collect { searchTerm ->
                    val sessions = searchSessions(searchTerm)
                    _uiState.emit(
                        _uiState.value.copy(
                            searchSessions = sessions
                        )
                    )
                }
        }
    }

    fun search(newEntry: String) {
        viewModelScope.launch {
            _uiState.emit(
                _uiState.value.copy(
                    searchTerm = newEntry
                )
            )
            searchEntry.emit(newEntry)
        }
    }

}

data class UIState(
    val discoverSessions: List<Session> = emptyList(),
    val searchSessions: List<Session> = emptyList(),
    val title: String = "Discover",
    val searchTerm: String = "",
) {
    fun getList(): List<Session> = if (searchTerm.isBlank()) discoverSessions else searchSessions
}

@Serializable
data class Response(
    val data: Data,
)

@Serializable
data class Data(
    val sessions: List<Session>,
)

@Serializable
data class Session(
    val name: String,
    @SerialName("listener_count") val listenerCount: Int,
    val genres: List<String>,
    @SerialName("current_track") val currentTrack: Track,
) {
    fun getGenres(): String = genres.joinToString(", ")
}

@Serializable
data class Track(
    val title: String,
    @SerialName("artwork_url") val art: String,
)