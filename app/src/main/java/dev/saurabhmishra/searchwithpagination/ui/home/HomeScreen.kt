package dev.saurabhmishra.searchwithpagination.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.saurabhmishra.searchwithpagination.routes.Route
import dev.saurabhmishra.searchwithpagination.ui.home.favourite.FavouriteScreen
import dev.saurabhmishra.searchwithpagination.ui.home.searchlist.SearchListScreen
import dev.saurabhmishra.searchwithpagination.ui.theme.SearchWithPaginationTheme

@Composable
fun HomeScreen() {
    val bottomNavigationItems = listOf(
        Route.Search,
        Route.Favourite
    )

    val navController = rememberNavController()

    Scaffold(
        bottomBar = { HomeBottomNavigation(bottomNavigationItems, navController) },
        content = { paddingValues ->
            NavigableContent(navController, paddingValues)
        }
    )
}


@Composable
private fun HomeBottomNavigation(
    bottomNavigationItems: List<Route>,
    navController: NavHostController
) {
    BottomNavigation {
        val currentEntry by navController.currentBackStackEntryAsState()
        val currentDestination = currentEntry?.destination
        bottomNavigationItems.forEach { bottomNavigationItem ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy?.any { it.route == bottomNavigationItem.name } == true,
                onClick = {
                    navController.navigate(bottomNavigationItem.name) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true

                        restoreState = true
                    }
                },
                icon = {
                    Icon(bottomNavigationItem.icon, contentDescription = bottomNavigationItem.name)
                },
                label = {
                    Text(text = bottomNavigationItem.name)
                },
                alwaysShowLabel = false
            )
        }
    }
}

@Composable
private fun NavigableContent(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Route.Search.name,
        modifier = Modifier.padding(
            bottom = paddingValues.calculateBottomPadding(),
            top = paddingValues.calculateTopPadding()
        )
    ) {
        composable(Route.Search.name) { SearchListScreen() }
        composable(Route.Favourite.name) { FavouriteScreen() }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    SearchWithPaginationTheme {
        HomeScreen()
    }
}




