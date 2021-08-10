package dev.saurabhmishra.searchwithpagination.mediator


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.sources.network.helper.SafeResult
import dev.saurabhmishra.searchwithpagination.utils.AppConstants
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher


@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
    private val searchRepo: SearchRepo
) : RemoteMediator<Int, PhotoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PhotoEntity>
    ): MediatorResult {

        val entity = when (loadType) {
            LoadType.APPEND -> {
                state.lastItemOrNull()
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.REFRESH -> null
        }

        val pageNumber = getPageNumberForCurrentEntity(entity)
        return loadAndSavePhotos(pageNumber = pageNumber)
    }

    private suspend fun getPageNumberForCurrentEntity(entity: PhotoEntity?): Int {

        entity ?: return AppConstants.INITIAL_PAGE

        return searchRepo.getPageNumberByPhotoId(entity.id)
    }

    private suspend fun loadAndSavePhotos(pageNumber: Int): MediatorResult {
        val currentQuery = SearchQueryPublisher.getCurrentQuery()

        return when (val photosResult = searchRepo.loadAndSavePhotos(currentQuery, pageNumber)) {
            is SafeResult.Success -> {
                val isReloadSuccess = photosResult.data.success

                if (isReloadSuccess) {
                    MediatorResult.Success(endOfPaginationReached = photosResult.data.endReached)
                } else {
                    MediatorResult.Error(SearchRefreshFailedException())
                }


            }
            is SafeResult.Failure -> {
                MediatorResult.Error(photosResult.exception)
            }
        }

    }

}

class SearchRefreshFailedException: Exception(message = "Search failed in either loading or saving in database")