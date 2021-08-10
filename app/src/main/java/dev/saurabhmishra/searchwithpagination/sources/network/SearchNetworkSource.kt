package dev.saurabhmishra.searchwithpagination.sources.network

import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.sources.network.helper.SafeResult
import dev.saurabhmishra.searchwithpagination.sources.network.helper.safeApiCall
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.CoroutineContextProvider

interface SearchNetworkSource {
  suspend fun searchPhotosForTag(searchQuery: String, pageNumber: Int): SafeResult<PhotosSearchResponse>
}

class SearchNetworkSourceImpl(
  private val coroutineContextProvider: CoroutineContextProvider,
  private val api: Api
): SearchNetworkSource {

  override suspend fun searchPhotosForTag(searchQuery: String, pageNumber: Int): SafeResult<PhotosSearchResponse> {
    return safeApiCall(coroutineContextProvider.ioThread) {
      api.searchPhotosWithTag(searchQuery, pageNumber)
    }
  }

}