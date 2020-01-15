package com.ehedgehog.android.spryrocksapp.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface TrelloApiService {

    @GET("boards/{boardId}/lists")
    fun getBoardLists(@Path("boardId") boardId: String): Deferred<List<BoardList>>

}