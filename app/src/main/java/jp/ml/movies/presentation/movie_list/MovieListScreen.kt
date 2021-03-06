package jp.ml.movies.presentation.movie_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jp.ml.movies.presentation.Screen
import jp.ml.movies.presentation.movie_list.components.MainAppBar
import jp.ml.movies.presentation.movie_list.components.MovieListItem
import jp.ml.movies.presentation.movie_list.components.SearchWidgetState


@Composable
fun MovieListScreen(
    navController: NavController,
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val searchWidgetState by viewModel.searchWidgetState
    val searchTextState by viewModel.searchTextState

    Scaffold(
        topBar = {
            MainAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    viewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                    viewModel.getPopularMovies()
                },
                onSearchClicked = {
                    viewModel.searchMovie(it)
                },
                onSearchTriggered = {
                    viewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                }
            )
        }
    ) {
        val state = viewModel.state.value
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {

            //Display no result message when no records found in remote DB
            if (state.movies.isEmpty() && !state.isLoading && state.error.isBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No matching result",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            //Display the result as grid items in the screen
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(150.dp)
            ) {
                items(state.movies) { movie ->
                    MovieListItem(
                        movie = movie,
                        onItemClick = {
                            navController.navigate(Screen.MovieDetailScreen.route + "/${movie.id}" + "/${false}")
                        }
                    )
                }
            }

            //If there is an error display message with retry button
            if (state.error.isNotBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .align(Alignment.Center),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                    )
                    TextButton(onClick = { viewModel.getPopularMovies() }) {
                        Text(
                            "RETRY",
                            fontSize = 16.sp,
                        )
                    }
                }
            }

            //Display progress indicator while reading the api
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
