package com.ianleshan.doublesymmetrytask

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class Greeting {
    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }

    private val client = HttpClient() {
        install(ContentNegotiation) {
            json()
        }
    }
    suspend fun getHtml(): List<Session> {
        return client
            .get("https://www.mocky.io/v2/5df79a3a320000f0612e0115")
            .body<Response>()
            .data
            .sessions
    }
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
)

@Serializable
data class Track(
    val title: String,
    @SerialName("artwork_url") val art: String,
)