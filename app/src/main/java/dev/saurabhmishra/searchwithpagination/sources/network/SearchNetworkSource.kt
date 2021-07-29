package dev.saurabhmishra.searchwithpagination.sources.network

import androidx.paging.*
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher


@OptIn(ExperimentalPagingApi::class)
class SearchNetworkSource(
  private val apiService: Api
): RemoteMediator<Int, PhotosSearchResponse>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, PhotosSearchResponse>
  ): MediatorResult {

    SearchQueryPublisher.getCurrentQuery()
  }

}