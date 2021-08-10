package dev.saurabhmishra.searchwithpagination.sources.local.dao

import androidx.room.*
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query("SELECT * FROM PhotoEntity WHERE searchQuery like :query")
    fun getPhotosForSearch(query: String): Flow<PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE isFavorite = 1")
    fun getFavoritePhotos(): Flow<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photoEntity: List<PhotoEntity>): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePhoto(photoEntity: PhotoEntity): Long

    @Query("DELETE FROM PhotoEntity")
    suspend fun deleteEverything(): Int
}