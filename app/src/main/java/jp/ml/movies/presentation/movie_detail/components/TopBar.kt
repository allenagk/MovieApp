package jp.ml.movies.presentation.movie_detail.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import jp.ml.movies.presentation.movie_detail.MovieDetailState

@Composable
fun TopBar(
    navController: NavController,
    movieDetailState: MovieDetailState,
    isSaved: Boolean, onExecuteFavorite: (Boolean) -> Unit
) {
    TopAppBar(
        title = { Text(text = "Movie Details") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = {
                onExecuteFavorite(isSaved)
            }, enabled = movieDetailState.movie != null) {
                Icon(
                    Icons.Filled.Favorite,
                    tint = if (isSaved) Color.Magenta else Color.Gray,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}