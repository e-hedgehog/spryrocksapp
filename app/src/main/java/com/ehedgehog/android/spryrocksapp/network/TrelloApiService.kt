package com.ehedgehog.android.spryrocksapp.network

import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface TrelloApiService {

    @GET("boards/{boardId}/lists")
    fun getBoardLists(@Path("boardId") boardId: String): Deferred<List<BoardList>>

    @POST("cards")
    fun createCardInBoardList(@Query("idList") idList: String,
                              @Query("name") name: String,
                              @Query("desc") description: String): Deferred<CardResponse>

    @PUT("cards/{cardId}")
    fun updateCardInBoardList(@Path("cardId") cardId: String,
                              @Query("name") name: String,
                              @Query("desc") description: String): Deferred<CardResponse>


    @POST("boards/{boardId}/lists")
    fun createListInBoard(@Path("boardId") boardId: String,
                          @Query("name") name: String): Deferred<BoardList>

}