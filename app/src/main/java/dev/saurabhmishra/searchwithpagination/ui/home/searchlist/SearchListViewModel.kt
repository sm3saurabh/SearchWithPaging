package dev.saurabhmishra.searchwithpagination.ui.home.searchlist

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dev.saurabhmishra.searchwithpagination.base.BaseViewModel
import dev.saurabhmishra.searchwithpagination.mediator.SearchRemoteMediator
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.utils.Logger
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@FlowPreview
class SearchListViewModel(
    private val searchRemoteMediator: SearchRemoteMediator,
    private val searchRepo: SearchRepo
): BaseViewModel() {


    init {
        initializeSearchQueryFlow()
    }

    @OptIn(ExperimentalPagingApi::class)
    private val pager: Pager<Int, PhotoEntity> = Pager(
        config = PagingConfig(5),
        remoteMediator = searchRemoteMediator
    ) {
        searchRepo.getPhotosForSearchQuery(SearchQueryPublisher.getCurrentQuery())
    }

    val photosResponseFlow = pager.flow.cachedIn(viewModelScope)

    val viewState: StateFlow<SearchListScreenViewState> = MutableStateFlow(SearchListScreenViewState.Idle)

    fun onEvent(event: SearchListScreenEvent) {
        when (event) {
            is SearchListScreenEvent.SearchQuery -> searchQueryRequested(event.query)
            is SearchListScreenEvent.ToggleFavourite -> toggleFavouriteRequested(event.photoEntity)
        }
    }

    private fun searchQueryRequested(query: String) {
        SearchQueryPublisher.setNewQuery(query)
    }

    private fun toggleFavouriteRequested(photoEntity: PhotoEntity) {
        viewModelScope.launch {
            searchRepo.togglePhotoAsFavourite(photoEntity)
        }
    }

    private fun initializeSearchQueryFlow() {
        SearchQueryPublisher.searchQuery.debounce(500L)
            .onEach { query ->
                if (query.isNotBlank()) {
                    viewState.setValue(SearchListScreenViewState.NewSearchQuery)
                }
            }.catch { throwable ->
                Logger.error("Error in search query processing", throwable)
            }.launchIn(viewModelScope)
    }
}

sealed class SearchListScreenEvent {
    class SearchQuery(val query: String): SearchListScreenEvent()
    class ToggleFavourite(val photoEntity: PhotoEntity): SearchListScreenEvent()
}

sealed class SearchListScreenViewState {
    object Idle: SearchListScreenViewState()
    object NewSearchQuery: SearchListScreenViewState()
}