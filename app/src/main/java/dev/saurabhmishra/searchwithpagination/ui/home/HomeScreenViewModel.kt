package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.utils.Logger
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
class HomeScreenViewModel(
  private val searchRepo: SearchRepo
) : ViewModel() {

  val searchQuery = MutableStateFlow("")

  init {
    searchQuery.debounce(500L)
      .onEach { query ->
        performSearchQueryApiCall()
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
    searchQuery.value = query
  }

  private suspend fun performSearchQueryApiCall() {
    searchRepo
  }



}

sealed class HomeScreenEvent {
  class SearchQuery(val query: String): HomeScreenEvent()
}