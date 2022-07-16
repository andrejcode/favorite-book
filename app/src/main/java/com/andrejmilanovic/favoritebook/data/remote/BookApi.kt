package com.andrejmilanovic.favoritebook.data.remote

import com.andrejmilanovic.favoritebook.data.remote.response.BookResponse
import com.andrejmilanovic.favoritebook.data.remote.response.BookItem
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BookApi {
    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): BookResponse

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): BookItem
}