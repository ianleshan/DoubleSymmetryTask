package com.ianleshan.doublesymmetrytask

import com.ianleshan.doublesymmetrytask.models.Session

data class UIState(
    val discoverSessions: List<Session> = emptyList(),
    val searchSessions: List<Session> = emptyList(),
    val title: String = "Discover",
    val searchTerm: String = "",
    val currentPage: Int = 0,
    val error: Exception? = null,
    val loading: Boolean = false,
) {
    fun getList(): List<Session> = if (searchTerm.isEmpty()) discoverSessions else searchSessions
    fun hasNextPage() = currentPage <= 5 && searchTerm.isEmpty()
}