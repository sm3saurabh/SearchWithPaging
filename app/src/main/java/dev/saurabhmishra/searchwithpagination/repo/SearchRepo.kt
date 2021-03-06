package dev.saurabhmishra.searchwithpagination.repo

import androidx.paging.PagingSource
import dev.saurabhmishra.searchwithpagination.mappers.toPhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoPageMetadata
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource
import dev.saurabhmishra.searchwithpagination.sources.network.helper.SafeResult
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.AppConstants
import dev.saurabhmishra.searchwithpagination.utils.Logger
import java.lang.Exception


data class SearchLoadResult(
    val success: Boolean,
    val endReached: Boolean
)

interface SearchRepo {
    suspend fun loadAndSavePhotos(searchQuery: String, pageNumber: Int): SafeResult<SearchLoadResult>
    suspend fun deleteEverything()
    fun getFavoritePhotos(): PagingSource<Int, PhotoEntity>
    fun getPhotosForSearchQuery(query: String): PagingSource<Int, PhotoEntity>
    suspend fun getPageNumberByPhotoId(id: String): Int
    suspend fun togglePhotoAsFavourite(photoEntity: PhotoEntity)
}

class SearchRepoImpl(
    private val searchNetworkSource: SearchNetworkSource,
    private val searchLocalSource: SearchLocalSource
) : SearchRepo {

    override suspend fun loadAndSavePhotos(
        searchQuery: String,
        pageNumber: Int
    ): SafeResult<SearchLoadResult> {

        return when (val photosResult =
            searchNetworkSource.searchPhotosForTag(searchQuery, pageNumber)) {
            is SafeResult.Success -> handlePhotoLoadSuccess(searchQuery, photosResult, pageNumber)
            is SafeResult.Failure -> photosResult
        }
    }

    override suspend fun deleteEverything() {
        searchLocalSource.deleteEverything()
    }

    override fun getFavoritePhotos(): PagingSource<Int, PhotoEntity> {
        return searchLocalSource.getFavoritePhotos()
    }

    override fun getPhotosForSearchQuery(query: String): PagingSource<Int, PhotoEntity> {
        return searchLocalSource.getPhotosForSearchQuery(query)
    }

    override suspend fun getPageNumberByPhotoId(id: String): Int {
        return searchLocalSource.getPhotoPageByPhotoId(id)
    }

    override suspend fun togglePhotoAsFavourite(photoEntity: PhotoEntity) {
        searchLocalSource.updatePhotoEntity(photoEntity.copy(isFavorite = !photoEntity.isFavorite))
    }

    private suspend fun handlePhotoLoadSuccess(
        searchQuery: String,
        photosResult: SafeResult.Success<PhotosSearchResponse>,
        pageNumber: Int
    ): SafeResult<SearchLoadResult> {

        val photos = photosResult.data.photos.photo

        val photoEntities = photos.map { photo ->
            photo.toPhotoEntity(searchQuery = searchQuery)
        }

        val photosMetaData = photos.map { photo ->
            PhotoPageMetadata(photo.id, pageNumber)
        }

        val endReached = photos.size < AppConstants.OBJECTS_PER_PAGE

        return try {
            val success = searchLocalSource.savePhotos(photoEntities)  &&
                searchLocalSource.savePhotoMetaData(photosMetaData)
            SafeResult.Success(SearchLoadResult(success = success, endReached = endReached))
        } catch (ex: Exception) {
            Logger.error("Exception while saving photos in db", ex)
            SafeResult.Failure(ex)
        }

    }

}