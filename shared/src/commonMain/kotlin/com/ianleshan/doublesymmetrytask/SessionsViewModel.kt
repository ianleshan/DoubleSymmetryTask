package com.ianleshan.doublesymmetrytask

import com.doublesymmetry.viewmodel.ViewModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SessionsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState

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

    fun onCreate() {
        viewModelScope.launch {
            val sessions = getSessions()
            val newState = _uiState.value.copy(
                sessions = sessions,
            )
            _uiState.emit(newState)
        }
    }
}

data class UIState(
    val sessions: List<Session> = emptyList(),
    val title: String = "Discover"
)

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