package dev.saurabhmishra.searchwithpagination.sources.network.api

import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("rest/")
    suspend fun searchPhotosWithTag(
        @Query("tags") searchQuery: String,
        @Query("page") page: Int
    ): PhotosSearchResponse
}