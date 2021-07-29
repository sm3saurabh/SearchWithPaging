package dev.saurabhmishra.searchwithpagination.ui.home

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.RemoteMediator
import androidx.paging.compose.collectAsLazyPagingItems
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource
import dev.saurabhmishra.searchwithpagination.sources.network.api.Api
import dev.saurabhmishra.searchwithpagination.utils.Logger
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.koin.androidx.compose.getKoin
import org.koin.core.context.KoinContext
import org.koin.java.KoinJavaComponent.inject

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
  private val searchNetworkSource: SearchNetworkSource
) : ViewModel() {

  val abc by inject<Api>(Api::class.java)

  init {
    SearchQueryPublisher.searchQuery.debounce(500L)
      .onEach { query ->
        performSearchQueryApiCall(query)
      }.catch { throwable ->
        Logger.error("Error in search query processing", throwable)
      }.launchIn(viewModelScope)
  }

  fun onEvent(event: HomeScreenEvent) {
    when (event) {
      is HomeScreenEvent.SearchQuery -> searchQueryRequested(event.query)
    }
  }

  private fun searchQueryRequested(query: String) {
    SearchQueryPublisher.setNewQuery(query)
  }

  @OptIn(ExperimentalPagingApi::class)
  private suspend fun performSearchQueryApiCall(query: String) {

    val pager = Pager(
      config = PagingConfig(10),
      remoteMediator = SearchNetworkSource(abc)
    ) {
      TODO()
    }

    pager.flow.collectAsLazyPagingItems()
  }



}

sealed class HomeScreenEvent {
  class SearchQuery(val query: String): HomeScreenEvent()
}