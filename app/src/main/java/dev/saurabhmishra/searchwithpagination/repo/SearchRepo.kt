package dev.saurabhmishra.searchwithpagination.repo

import dev.saurabhmishra.searchwithpagination.mappers.toPhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource
import dev.saurabhmishra.searchwithpagination.sources.network.helper.SafeResult
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotoResponse
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.Logger
import kotlinx.coroutines.flow.Flow
import java.lang.Exception


interface SearchRepo {
    suspend fun loadAndSavePhotos(searchQuery: String, pageNumber: Int): SafeResult<Boolean>
    suspend fun deleteEverything()
    fun getFavoritePhotos(): Flow<PhotoEntity>
    fun getPhotosForSearchQuery(query: String): Flow<PhotoEntity>
}

class SearchRepoImpl(
    private val searchNetworkSource: SearchNetworkSource,
    private val searchLocalSource: SearchLocalSource
) : SearchRepo {

    override suspend fun loadAndSavePhotos(
        searchQuery: String,
        pageNumber: Int
    ): SafeResult<Boolean> {

        return when (val photosResult =
            searchNetworkSource.searchPhotosForTag(searchQuery, pageNumber)) {
            is SafeResult.Success -> handlePhotoLoadSuccess(searchQuery, photosResult)
            is SafeResult.Failure -> photosResult
        }
    }

    override suspend fun deleteEverything() {
        searchLocalSource.deleteEverything()
    }

    override fun getFavoritePhotos(): Flow<PhotoEntity> {
        return searchLocalSource.getFavoritePhotos()
    }

    override fun getPhotosForSearchQuery(query: String): Flow<PhotoEntity> {
        return searchLocalSource.getPhotosForSearchQuery(query)
    }

    private suspend fun handlePhotoLoadSuccess(
        searchQuery: String,
        photosResult: SafeResult.Success<PhotosSearchResponse>
    ): SafeResult<Boolean> {

        val photos = photosResult.data.photos.photo

        val photoEntities = photos.map { photo -> photo.toPhotoEntity(searchQuery = searchQuery) }

        return try {
            val success = searchLocalSource.savePhotos(photoEntities)
            SafeResult.Success(success)
        } catch (ex: Exception) {
            Logger.error("Exception while saving photos in db", ex)
            SafeResult.Failure(ex)
        }

    }

}