package com.ehedgehog.android.spryrocksapp.network

data class ListsResponse(
    val boardLists: List<BoardList>
)

data class BoardList(
    val id: String,
    val name: String,
    val closed: Boolean)