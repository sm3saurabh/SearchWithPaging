package dev.saurabhmishra.searchwithpagination.mediator


import androidx.paging.*
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher


@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
  private val searchRepo: SearchRepo
): RemoteMediator<Int, PhotosSearchResponse>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, PhotosSearchResponse>
  ): MediatorResult {

    val currentQuery = SearchQueryPublisher.getCurrentQuery()
    searchRepo
  }

}