package dev.saurabhmishra.searchwithpagination.repo

import dev.saurabhmishra.searchwithpagination.sources.network.SearchNetworkSource

interface SearchRepo {

}

class SearchRepoImpl(
    private val searchNetworkSource: SearchNetworkSource
): SearchRepo {

}