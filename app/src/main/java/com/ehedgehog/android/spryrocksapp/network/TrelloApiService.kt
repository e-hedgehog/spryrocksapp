package com.ehedgehog.android.spryrocksapp.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TrelloApiService {

    @GET("boards/{boardId}/lists")
    fun getBoardLists(@Path("boardId") boardId: String): Deferred<List<BoardList>>

    @POST("cards")
    fun createCardInBoardList(@Query("idList") idList: String,
                              @Query("name") name: String,
                              @Query("desc") description: String): Deferred<CardsResponse>

}