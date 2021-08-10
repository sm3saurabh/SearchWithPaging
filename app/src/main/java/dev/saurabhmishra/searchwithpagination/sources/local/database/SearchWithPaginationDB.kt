package dev.saurabhmishra.searchwithpagination.sources.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.saurabhmishra.searchwithpagination.sources.local.dao.PhotoDao
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class SearchWithPaginationDB: RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}