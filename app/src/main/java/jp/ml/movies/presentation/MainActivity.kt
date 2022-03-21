package jp.ml.movies.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.ml.movies.presentation.favorites.FavoriteScreen
import jp.ml.movies.presentation.movie_detail.MovieDetailScreen
import jp.ml.movies.presentation.movie_list.MovieListScreen
import jp.ml.movies.presentation.movie_list.MovieListViewModel
import jp.ml.movies.presentation.ui.theme.MoviesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieListViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition() {
                viewModel.state.value.isLoading
            }
        }
        setContent {
            MoviesTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberAnimatedNavController()

                // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
                // Not all routes need to show the bottom bar.
                val bottomBarTabs = listOf(Screen.MovieListScreen, Screen.FavoriteScreen)
                val bottomBarRoutes = bottomBarTabs.map { it.route }
                val shouldShowBottomBar: Boolean = navController
                    .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

                Scaffold(
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) {
                    Box(modifier = Modifier.padding(it)) {
                        Navigation(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {

    val items = listOf(
        Screen.MovieListScreen,
        Screen.FavoriteScreen,
    )

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Black,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { items ->
            BottomNavigationItem(
                icon = {
                    items.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = items.title
                        )
                    }
                },
                label = { Text(text = items.title) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == items.route,
                onClick = {
                    navController.navigate(items.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route = route) {
                                saveState = true
                            }
                        }

                        launchSingleTop = true
                        restoreState = true
                    }

                }
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation(navController: NavHostController) {

    AnimatedNavHost(navController, startDestination = Screen.MovieListScreen.route) {

        composable(
            route = Screen.MovieListScreen.route,
            exitTransition = {
                when (targetState.destination.route) {
                    "movie_detail_screen/{movieId}" ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            popEnterTransition = {
                when (initialState.destination.route) {
                    "movie_detail_screen/{movieId}" ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            MovieListScreen(navController)
        }

        composable(route = Screen.FavoriteScreen.route) {
            FavoriteScreen(navController)
        }

        composable(
            route = Screen.MovieDetailScreen.route + "/{movieId}",
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentScope.SlideDirection.Left,
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentScope.SlideDirection.Right,
                    animationSpec = tween(300)
                )
            }
        ) {
            MovieDetailScreen(navController)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoviesTheme {

    }
}