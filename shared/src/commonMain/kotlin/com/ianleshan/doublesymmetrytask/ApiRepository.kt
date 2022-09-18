package com.ianleshan.doublesymmetrytask

import com.ianleshan.doublesymmetrytask.models.Response
import com.ianleshan.doublesymmetrytask.models.Session
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*

class ApiRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun getSessions(): List<Session> {
        return client
            .get("https://www.mocky.io/v2/5df79a3a320000f0612e0115")
            .body<Response>()
            .data
            .sessions
    }

    suspend fun searchSessions(
        searchTerm: String // Not used
    ): List<Session> {
        return client
            .get("https://www.mocky.io/v2/5df79b1f320000f4612e011e")
            .body<Response>()
            .data
            .sessions
            .shuffled()
    }


}