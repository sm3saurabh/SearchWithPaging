package dev.saurabhmishra.searchwithpagination.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * I don't like this, but paging doesn't really allow to pass
 * arguments to the remote mediator, this hack works for now
 * Remote mediator will use current search query when trying to fetch new data according to a changed search query
 * The pager is formed using room database which uses current search query to return relevant data
 *
 * */
object SearchQueryPublisher {
  private val currentSearchQuery = MutableStateFlow("")
  val searchQuery = currentSearchQuery.asStateFlow()

  fun setNewQuery(query: String) {
    currentSearchQuery.value = query
  }

  fun getCurrentQuery(): String {
    return currentSearchQuery.value
  }
}