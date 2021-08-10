package dev.saurabhmishra.searchwithpagination.routes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(val name: String, val icon: ImageVector) {
    object Home: Route("Home", Icons.Outlined.Home)
    object Search: Route("Search", Icons.Outlined.Search)
    object Favourite: Route("Favourite", Icons.Outlined.Favorite)
}
