package dev.saurabhmishra.searchwithpagination.ui.home.favourite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import dev.saurabhmishra.searchwithpagination.ui.widgets.ImageList
import org.koin.androidx.compose.getViewModel

@Composable
fun FavouriteScreen() {
    val viewModel = getViewModel<FavouriteViewModel>()

    val photoPagingItems = viewModel.photosPagingFlow.collectAsLazyPagingItems()

    Surface(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        ImageList(photosPagingItems = photoPagingItems, onLikeIconClick = { photo ->
            viewModel.toggleFavouriteRequested(photoEntity = photo)
        })
    }
}