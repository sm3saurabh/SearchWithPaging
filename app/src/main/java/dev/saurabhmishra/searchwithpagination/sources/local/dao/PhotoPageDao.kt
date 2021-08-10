package dev.saurabhmishra.searchwithpagination.sources.local.dao

import androidx.room.*
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoPageMetadata

@Dao
interface PhotoPageDao {

    @Query("SELECT * FROM PhotoPageMetadata WHERE photoId = :id")
    suspend fun getMetaDataById(id: String): PhotoPageMetadata?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPageMetaData(metaData: List<PhotoPageMetadata>): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePageMetaData(metaData: PhotoPageMetadata): Long

    @Query("DELETE FROM PhotoPageMetadata")
    suspend fun deleteEverything(): Int
}