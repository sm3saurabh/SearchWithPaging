package dev.saurabhmishra.searchwithpagination.sources.local.entities

import androidx.room.Entity

@Entity
data class PhotoEntity(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val isFavorite: Boolean,
    val searchQuery: String
)