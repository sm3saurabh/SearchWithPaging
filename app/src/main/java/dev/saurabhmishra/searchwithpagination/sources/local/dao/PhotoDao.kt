package dev.saurabhmishra.searchwithpagination.sources.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity

@Dao
interface PhotoDao {
    @Query("SELECT * FROM PhotoEntity WHERE searchQuery like :query")
    fun getPhotosForSearch(query: String): PagingSource<Int, PhotoEntity>

    @Query("SELECT * FROM PhotoEntity WHERE isFavorite = 1")
    fun getFavoritePhotos(): PagingSource<Int, PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photoEntity: List<PhotoEntity>): List<Long>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePhoto(photoEntity: PhotoEntity)

    @Query("DELETE FROM PhotoEntity")
    suspend fun deleteEverything(): Int
}