package dev.saurabhmishra.searchwithpagination.sources.network.api

import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse

interface Api {
    fun searchPhotosWithTag(
        tag: String
    ): PhotosSearchResponse
}