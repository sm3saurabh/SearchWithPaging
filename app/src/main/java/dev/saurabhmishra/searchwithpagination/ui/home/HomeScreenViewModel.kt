package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import dev.saurabhmishra.searchwithpagination.base.BaseViewModel
import dev.saurabhmishra.searchwithpagination.mediator.SearchRemoteMediator
import dev.saurabhmishra.searchwithpagination.sources.network.models.PhotosSearchResponse
import dev.saurabhmishra.searchwithpagination.utils.Logger
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class, ExperimentalPagingApi::class)
class HomeScreenViewModel(
  searchRemoteMediator: SearchRemoteMediator
) : BaseViewModel() {


  init {
    initializeSearchQueryFlow()
  }

  private val pager: Pager<Int, PhotosSearchResponse> = Pager(
    config = PagingConfig(5),
    remoteMediator = searchRemoteMediator
  ) {
    TODO()
  }

  val photosResponseFlow = pager.flow

  val viewState: StateFlow<HomeScreenViewState> = MutableStateFlow(HomeScreenViewState.Idle)

  fun onEvent(event: HomeScreenEvent) {
    when (event) {
      is HomeScreenEvent.SearchQuery -> searchQueryRequested(event.query)
    }
  }

  private fun searchQueryRequested(query: String) {
    SearchQueryPublisher.setNewQuery(query)
  }

  private fun initializeSearchQueryFlow() {
    SearchQueryPublisher.searchQuery.debounce(500L)
      .onEach {
        viewState.setValue(HomeScreenViewState.Searching)
      }.catch { throwable ->
        Logger.error("Error in search query processing", throwable)
      }.launchIn(viewModelScope)
  }



}

sealed class HomeScreenEvent {
  class SearchQuery(val query: String): HomeScreenEvent()
}

sealed class HomeScreenViewState {
  object Idle: HomeScreenViewState()
  object Searching: HomeScreenViewState()
}