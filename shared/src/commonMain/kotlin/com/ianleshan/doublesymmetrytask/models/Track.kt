package com.ianleshan.doublesymmetrytask.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Track(
    val title: String,
    @SerialName("artwork_url") val art: String,
)