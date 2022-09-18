package com.ianleshan.doublesymmetrytask.models

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val sessions: List<Session>,
)