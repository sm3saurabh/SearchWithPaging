package dev.saurabhmishra.searchwithpagination.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.saurabhmishra.searchwithpagination.ui.home.HomeScreen
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme
import dev.saurabhmishra.searchwithpagination.utils.AppConstants

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SearchWithPaginationTheme {
        HomeScreen()
      }
    }
  }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
  SearchWithPaginationTheme {
    HomeScreen()
  }
}