package dev.saurabhmishra.searchwithpagination.ui.home.searchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import dev.saurabhmishra.searchwithpagination.mappers.getPhotoUrl
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import kotlinx.coroutines.flow.flow
import org.koin.androidx.compose.getViewModel


@Composable
fun SearchListScreen() {

    val currentSearchQuery by SearchQueryPublisher.searchQuery.collectAsState()
    val viewModel = getViewModel<SearchListViewModel>()

    val photosPagingItems = viewModel.photosResponseFlow.collectAsLazyPagingItems()
    val viewState by viewModel.viewState.collectAsState()

    Column {
        SearchBar(textToShow = currentSearchQuery, inputChanged = { query ->
            viewModel.onEvent(SearchListScreenEvent.SearchQuery(query))
        })
        ImageList(photosPagingItems = photosPagingItems, viewState = viewState)
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

@Composable
private fun ImageList(
    photosPagingItems: LazyPagingItems<PhotoEntity>,
    viewState: SearchListScreenViewState
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(photosPagingItems) { photo ->
            photo?.let {
                Image(
                    painter = rememberImagePainter(photo.getPhotoUrl()),
                    contentDescription = photo.title
                )
            }
        }
    }

    if (viewState is SearchListScreenViewState.NewSearchQuery) {
        photosPagingItems.refresh()
    }
}

@Preview
@Composable
fun SearchBarPreview() {
    SearchWithPaginationTheme {
        SearchBar(textToShow = "Whoa", inputChanged = {})
    }
}

@Preview
@Composable
fun ImageListPreview() {
    val flowing = flow<PagingData<PhotoEntity>> {  }
    val photoPagingItems = flowing.collectAsLazyPagingItems()

    SearchWithPaginationTheme {
        ImageList(photosPagingItems = photoPagingItems, viewState = SearchListScreenViewState.Idle)
    }
}