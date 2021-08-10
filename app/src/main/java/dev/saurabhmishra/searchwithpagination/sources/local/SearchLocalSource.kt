package dev.saurabhmishra.searchwithpagination.sources.local

import androidx.paging.PagingSource
import dev.saurabhmishra.searchwithpagination.sources.local.dao.PhotoDao
import dev.saurabhmishra.searchwithpagination.sources.local.dao.PhotoPageDao
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoPageMetadata
import dev.saurabhmishra.searchwithpagination.utils.AppConstants
import dev.saurabhmishra.searchwithpagination.utils.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface SearchLocalSource {
    suspend fun savePhotos(photos: List<PhotoEntity>): Boolean
    fun getPhotosForSearchQuery(query: String): PagingSource<Int, PhotoEntity>
    fun getFavoritePhotos(): Flow<PhotoEntity>
    suspend fun deleteEverything()
    suspend fun getPhotoPageByPhotoId(id: String): Int
    suspend fun savePhotoMetaData(photoPageMetadata: List<PhotoPageMetadata>): Boolean
}

class SearchLocalSourceImpl(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val photoDao: PhotoDao,
    private val photoPageDao: PhotoPageDao
): SearchLocalSource {

    override suspend fun savePhotos(photos: List<PhotoEntity>): Boolean {
        return withContext(coroutineContextProvider.ioThread) {
            photoDao.insertPhotos(photos).size >= 0
        }
    }

    override fun getPhotosForSearchQuery(query: String): PagingSource<Int, PhotoEntity> {
        return photoDao.getPhotosForSearch(query = query)
    }

    override fun getFavoritePhotos(): Flow<PhotoEntity> {
        return photoDao.getFavoritePhotos()
            .flowOn(coroutineContextProvider.ioThread)
    }

    override suspend fun deleteEverything() {
        withContext(coroutineContextProvider.ioThread) {
            photoDao.deleteEverything()
            photoPageDao.deleteEverything()
        }
    }

    override suspend fun getPhotoPageByPhotoId(id: String): Int {
        val metaData = photoPageDao.getMetaDataById(id)
        return metaData?.pageNumber ?: AppConstants.INITIAL_PAGE
    }

    override suspend fun savePhotoMetaData(photoPageMetadata: List<PhotoPageMetadata>): Boolean {
        return withContext(coroutineContextProvider.ioThread) {
            photoPageDao.insertPageMetaData(photoPageMetadata).size >= 0
        }
    }

}
