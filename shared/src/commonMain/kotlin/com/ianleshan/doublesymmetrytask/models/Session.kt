package com.ianleshan.doublesymmetrytask.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val name: String,
    @SerialName("listener_count") val listenerCount: Int,
    val genres: List<String>,
    @SerialName("current_track") val currentTrack: Track,
) {
    fun getGenres(): String = genres.joinToString(", ")
}