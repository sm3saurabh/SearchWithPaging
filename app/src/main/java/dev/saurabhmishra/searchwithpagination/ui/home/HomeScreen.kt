package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.saurabhmishra.searchwithpagination.R
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import dev.saurabhmishra.searchwithpagination.utils.SearchQueryPublisher
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen() {
  val viewModel = getViewModel<HomeScreenViewModel>()
  val currentSearchQuery by SearchQueryPublisher.searchQuery.collectAsState()

  HomeScreenContent(textToShow = currentSearchQuery, inputChanged = { searchQuery ->
    viewModel.onEvent(HomeScreenEvent.SearchQuery(searchQuery))
  })
}


@Composable
private fun HomeScreenContent(textToShow: String, inputChanged: (String) -> Unit) {
  Scaffold(
    bottomBar = { HomeBottomNavigation() }
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
private fun ImageList() {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(100) { index ->
      Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Sample image"
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
  }
}

@Composable
@Preview
fun HomeScreenPreview() {
  SearchWithPaginationTheme {
    HomeScreenContent(textToShow = "What the hell", inputChanged = {})
  }
}