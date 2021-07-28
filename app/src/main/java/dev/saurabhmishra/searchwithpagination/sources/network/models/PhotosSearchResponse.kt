package dev.saurabhmishra.searchwithpagination.sources.network.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosSearchResponse(
    val photos: PhotosMetaData
)

@JsonClass(generateAdapter = true)
data class PhotosMetaData(
    val page: Int,
    val photo: List<PhotoResponse>
)

@JsonClass(generateAdapter = true)
data class PhotoResponse(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)