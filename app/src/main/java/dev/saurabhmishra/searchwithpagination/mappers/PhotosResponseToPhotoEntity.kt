package dev.saurabhmishra.searchwithpagination.mappers

import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotoResponse

fun PhotoResponse.toPhotoEntity(searchQuery: String): PhotoEntity {
    return PhotoEntity(
        id = this.id,
        owner = this.owner,
        secret = this.secret,
        server = this.server,
        farm = this.farm,
        title = this.title,
        isFavorite = false,
        searchQuery = searchQuery
    )
}

fun PhotoEntity.toPhotoResponse(): PhotoResponse {
    return PhotoResponse(
        id = this.id,
        owner = this.owner,
        secret = this.secret,
        server = this.server,
        farm = this.farm,
        title = this.title
    )
}