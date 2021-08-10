package dev.saurabhmishra.searchwithpagination.sources.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoEntity(
    @PrimaryKey val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String,
    val isFavorite: Boolean,
    val searchQuery: String
)