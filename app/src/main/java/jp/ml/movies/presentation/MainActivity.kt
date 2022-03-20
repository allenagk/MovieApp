package jp.ml.movies.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jp.ml.movies.presentation.movie_detail.MovieDetailScreen
import jp.ml.movies.presentation.movie_list.MovieListScreen
import jp.ml.movies.presentation.movie_list.MovieListViewModel
import jp.ml.movies.presentation.ui.theme.MoviesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MovieListViewModel by viewModels()

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
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.MovieListScreen.route
                    ) {
                        composable(
                            route = Screen.MovieListScreen.route
                        ) {
                            MovieListScreen(navController)
                        }
                        composable(
                            route = Screen.MovieDetailScreen.route + "/{movieId}"
                        ) {
                            MovieDetailScreen()
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MoviesTheme {

    }
}