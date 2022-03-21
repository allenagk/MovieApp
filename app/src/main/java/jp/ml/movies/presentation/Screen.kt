package jp.ml.movies.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Destinations used in the [movies App].
 */
sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object MovieListScreen : Screen("movie_list_screen", "Home", Icons.Default.Home)
    object MovieDetailScreen : Screen("movie_detail_screen", "Movie", null)
    object FavoriteScreen : Screen("favorite", "Favorites", Icons.Default.Favorite)
}
