package dev.saurabhmishra.searchwithpagination.ui.home.searchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import dev.saurabhmishra.searchwithpagination.mappers.getPhotoUrl
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import dev.saurabhmishra.searchwithpagination.ui.widgets.ImageList
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.getViewModel


@Composable
fun SearchListScreen() {

    val currentSearchQuery by SearchQueryPublisher.searchQuery.collectAsState()
    val viewModel = getViewModel<SearchListViewModel>()

    val photosPagingItems = viewModel.photosResponseFlow.collectAsLazyPagingItems()
    val viewState by viewModel.viewState.collectAsState()

    if (viewState is SearchListScreenViewState.NewSearchQuery) {
        photosPagingItems.refresh()
    }

    Column(modifier = Modifier.padding(12.dp)) {
        SearchBar(textToShow = currentSearchQuery, inputChanged = { query ->
            viewModel.onEvent(SearchListScreenEvent.SearchQuery(query))
        })
        Spacer(modifier = Modifier.height(12.dp))
        ImageList(photosPagingItems = photosPagingItems, onLikeIconClick = { photo ->
            viewModel.onEvent(SearchListScreenEvent.ToggleFavourite(photo))
        })
    }
}

@Composable
private fun SearchBar(
    textToShow: String,
    inputChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = textToShow,
        label = {
            Text(text = "Search input")
        },
        onValueChange = inputChanged,
        textStyle = MaterialTheme.typography.body1,
        modifier = Modifier
            .fillMaxWidth()
    )
}



@Preview
@Composable
fun SearchBarPreview() {
    SearchWithPaginationTheme {
        SearchBar(textToShow = "Whoa", inputChanged = {})
    }
}