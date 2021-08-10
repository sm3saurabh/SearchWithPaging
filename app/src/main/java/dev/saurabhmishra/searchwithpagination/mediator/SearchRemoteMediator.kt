package dev.saurabhmishra.searchwithpagination.mediator


import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher


@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
  private val searchRepo: SearchRepo
): RemoteMediator<Int, PhotoEntity>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, PhotoEntity>
  ): MediatorResult {


    val currentQuery = SearchQueryPublisher.getCurrentQuery()
    searchRepo.loadAndSavePhotos(currentQuery, pageNumber = )
  }

}