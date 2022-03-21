package jp.ml.movies.presentation.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import jp.ml.movies.presentation.Screen
import jp.ml.movies.presentation.movie_list.components.MovieListItem


@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoritesViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Favorites") }
            )
        }
    ) {
        val state = viewModel.state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {

            if (state.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Favorites",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(150.dp)
            ) {
                items(state) { favorite ->
                    MovieListItem(
                        movie = favorite.toMovie(),
                        onItemClick = {
                            navController.navigate(Screen.MovieDetailScreen.route + "/${favorite.id}" + "/${true}")
                        }
                    )
                }
            }
        }
    }
}
