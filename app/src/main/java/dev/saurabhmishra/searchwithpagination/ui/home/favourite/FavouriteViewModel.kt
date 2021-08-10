package dev.saurabhmishra.searchwithpagination.ui.home.favourite

import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dev.saurabhmishra.searchwithpagination.base.BaseViewModel
import dev.saurabhmishra.searchwithpagination.repo.SearchRepo
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.utils.AppConstants
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val searchRepo: SearchRepo
): BaseViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    private val pager: Pager<Int, PhotoEntity> = Pager(
        config = PagingConfig(AppConstants.OBJECTS_PER_PAGE)
    ) {
        searchRepo.getFavoritePhotos()
    }

    val photosPagingFlow = pager.flow.cachedIn(viewModelScope)


    fun toggleFavouriteRequested(photoEntity: PhotoEntity) {
        viewModelScope.launch {
            searchRepo.togglePhotoAsFavourite(photoEntity)
        }
    }

}