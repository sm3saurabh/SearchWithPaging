package dev.saurabhmishra.searchwithpagination.sources.network.api

import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import retrofit2.http.GET

interface Api {

    @GET
    fun searchPhotosWithTag(

    ): PhotosSearchResponse
}