package dev.saurabhmishra.searchwithpagination.sources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoPageMetadata(
    @PrimaryKey val photoId: String,
    val pageNumber: Int
)
