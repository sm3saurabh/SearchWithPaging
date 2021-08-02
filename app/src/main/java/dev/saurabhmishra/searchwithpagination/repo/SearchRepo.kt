package dev.saurabhmishra.searchwithpagination.repo

import dev.saurabhmishra.searchwithpagination.sources.local.SearchLocalSource
import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource


interface SearchRepo {

}

class SearchRepoImpl(
  private val searchNetworkSource: SearchNetworkSource,
  private val searchLocalSource: SearchLocalSource
) : SearchRepo {

}