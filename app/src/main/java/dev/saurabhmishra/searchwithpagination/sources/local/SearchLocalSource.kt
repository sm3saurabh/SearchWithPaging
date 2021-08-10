package dev.saurabhmishra.searchwithpagination.sources.local

import dev.saurabhmishra.searchwithpagination.sources.local.dao.PhotoDao
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.utils.CoroutineContextProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

interface SearchLocalSource {
    suspend fun savePhotos(photos: List<PhotoEntity>): Boolean
    fun getPhotosForSearchQuery(query: String): Flow<PhotoEntity>
    fun getFavoritePhotos(): Flow<PhotoEntity>
    suspend fun deleteEverything()
}

class SearchLocalSourceImpl(
    private val coroutineContextProvider: CoroutineContextProvider,
    private val photoDao: PhotoDao
): SearchLocalSource {

    override suspend fun savePhotos(photos: List<PhotoEntity>): Boolean {
        return withContext(coroutineContextProvider.ioThread) {
            photoDao.insertPhotos(photos) >= 0
        }
    }

    override fun getPhotosForSearchQuery(query: String): Flow<PhotoEntity> {
        return photoDao.getPhotosForSearch(query = query)
            .flowOn(coroutineContextProvider.ioThread)
    }

    override fun getFavoritePhotos(): Flow<PhotoEntity> {
        return photoDao.getFavoritePhotos()
            .flowOn(coroutineContextProvider.ioThread)
    }

    override suspend fun deleteEverything() {
        withContext(coroutineContextProvider.ioThread) {
            photoDao.deleteEverything()
        }
    }

}
