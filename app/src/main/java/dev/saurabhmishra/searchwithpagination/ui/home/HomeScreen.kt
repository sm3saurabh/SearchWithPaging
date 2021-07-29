package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import dev.saurabhmishra.searchwithpagination.R
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import org.koin.androidx.compose.getViewModel
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
  val viewModel = getViewModel<HomeScreenViewModel>()
  HomeScreenContent(viewModel)
}

@Composable
fun HomeScreenContent(viewModel: HomeScreenViewModel) {
  val textToShow by viewModel.searchQuery.collectAsState()

  Surface {

    // here we use LazyColumn that has build-in nested scroll, but we want to act like a
// parent for this LazyColumn and participate in its nested scroll.
// Let's make a collapsing toolbar for LazyColumn
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
// our offset to collapse toolbar
    val toolbarOffsetHeightPx =

      remember { mutableStateOf(0f) }
// now, let's create connection to the nested scroll system and listen to the scroll
// happening inside child LazyColumn
    val nestedScrollConnection = remember {
      object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
          // try to consume before LazyColumn to collapse toolbar if needed, hence pre-scroll
          val delta = available.y
          val newOffset = toolbarOffsetHeightPx.value + delta
          toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
          // here's the catch: let's pretend we consumed 0 in any case, since we want
          // LazyColumn to scroll anyway for good UX
          // We're basically watching scroll without taking it
          return Offset.Zero
        }
      }
    }

    Box(
      Modifier
        .fillMaxSize()
        // attach as a parent to the nested scroll system
        .nestedScroll(nestedScrollConnection)
    ) {
      // our list with build in nested scroll support that will notify us about its scroll
      LazyColumn(contentPadding = PaddingValues(top = toolbarHeight), modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally) {
        items(100) { index ->
          Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Sample image"
          )
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
      TopAppBar(
        modifier = Modifier
          .height(toolbarHeight)
          .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
        title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") }
      )
    }

//    TopAppBar(
//      backgroundColor = Color.Transparent
//    ) {
//      SearchBar(textToShow, inputChanged = { input ->
//        viewModel.onEvent(HomeScreenEvent.SearchQuery(input))
//      })
//    }
//
//    ImageList()
//    Column(
//      horizontalAlignment = Alignment.CenterHorizontally,
//      modifier = Modifier
//        .fillMaxWidth()
//        .fillMaxHeight()
//    ) {
//
//
//      Spacer(modifier = Modifier.height(12.dp))
//
//
//    }

  }
}


@Composable
fun SearchBar(textToShow: String, inputChanged: (String) -> Unit) {
  OutlinedTextField(
    value = textToShow,
    label = {
      Text(text = "Search input")
    },
    onValueChange = inputChanged,
    textStyle = MaterialTheme.typography.body1,
    modifier = Modifier.fillMaxWidth()
  )
}

@Composable
fun ImageList() {
  LazyColumn(content = {
    items(20) {
      Image(
        painter = painterResource(id = R.drawable.ic_launcher_background),
        contentDescription = "Sample image"
      )
      Spacer(modifier = Modifier.height(12.dp))
    }
  })
}

@Composable
@Preview
fun HomeScreenPreview() {
  SearchWithPaginationTheme {
    Surface(modifier = Modifier.padding(all = 12.dp)) {
      SearchBar("", inputChanged = {})
    }
  }
}