package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import dev.saurabhmishra.searchwithpagination.mappers.getPhotoUrl
import dev.saurabhmishra.searchwithpagination.sources.local.entities.PhotoEntity
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen() {
    val viewModel = getViewModel<HomeScreenViewModel>()
    val currentSearchQuery by SearchQueryPublisher.searchQuery.collectAsState()

    val viewState by viewModel.viewState.collectAsState()
    val photosPagingItems = viewModel.photosResponseFlow.collectAsLazyPagingItems()

    HomeScreenContent(currentSearchQuery, viewState, photosPagingItems) { searchQuery ->
        viewModel.onEvent(HomeScreenEvent.SearchQuery(searchQuery))
    }
}


@Composable
private fun HomeScreenContent(
    textToShow: String,
    viewState: HomeScreenViewState,
    photosPagingItems: LazyPagingItems<PhotoEntity>,
    inputChanged: (String) -> Unit
) {
    Scaffold(
        bottomBar = { HomeBottomNavigation() },
        content = {
            SearchBar(textToShow = textToShow, inputChanged = inputChanged)
            ImageList(photosPagingItems, viewState)
        }
    )
}

@Composable
private fun HomeBottomNavigation() {
    BottomNavigation {

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
    viewState: HomeScreenViewState
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

    if (viewState is HomeScreenViewState.NewSearchQuery) {
        photosPagingItems.refresh()
    }
}
