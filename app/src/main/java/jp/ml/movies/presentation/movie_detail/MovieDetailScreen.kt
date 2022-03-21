package jp.ml.movies.presentation.movie_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import jp.ml.movies.common.Utils
import jp.ml.movies.presentation.movie_detail.components.MovieGenre
import jp.ml.movies.presentation.movie_detail.components.ShimmerAnimation
import jp.ml.movies.presentation.movie_detail.components.TopBar
import jp.ml.movies.presentation.movie_detail.components.UserScore
import kotlinx.coroutines.flow.collectLatest


@Composable
fun MovieDetailScreen(
    navController: NavController,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()

    //Listen for the favorite movie save/delete user event
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MovieDetailViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is MovieDetailViewModel.UiEvent.SaveFavorite -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController, state, viewModel.isSavedState.value) { isSaved ->
                state.movie?.let { viewModel.saveOrDeleteMovie(it, isSaved) }
            }
        },
        scaffoldState = scaffoldState
    ) {
        Box(modifier = Modifier.padding(it)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                state.movie?.let { movie ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {
                            SubcomposeAsyncImage(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://www.themoviedb.org/t/p/w1280${movie.backDropPath}")
                                    .crossfade(true)
                                    .build(),
                                loading = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.White)
                                    ) {
                                        ShimmerAnimation()
                                    }
                                },
                                contentDescription = "poster image",
                                contentScale = ContentScale.Crop,
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "${movie.title}",
                                    style = MaterialTheme.typography.h4,
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                val score = movie.voteAverage?.times(10)
                                if (score != null) {
                                    UserScore("User Score", score.toInt(), 100)
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                movie.tagline?.let { tagline ->
                                    Text(
                                        text = tagline,
                                        fontStyle = FontStyle.Italic,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                movie.overview?.let { overView ->
                                    Text(
                                        text = overView,
                                        style = MaterialTheme.typography.body2
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Genres",
                                    style = MaterialTheme.typography.h6
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                FlowRow(
                                    mainAxisSpacing = 10.dp,
                                    crossAxisSpacing = 10.dp,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    movie.genres.forEach { genre ->
                                        if (genre.isNotBlank()) {
                                            MovieGenre(tag = genre)
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Duration: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = Utils.getFormattedDuration(movie.runtime),
                                        style = MaterialTheme.typography.subtitle1,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Status: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = "${movie.status}",
                                        style = MaterialTheme.typography.subtitle1,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Released Date: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    movie.releaseDate?.let { releaseDate ->
                                        Text(
                                            text = releaseDate,
                                            style = MaterialTheme.typography.subtitle1,
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Original Language: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = Utils.getDisplayLanguage(movie.originalLanguage),
                                        style = MaterialTheme.typography.subtitle1,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Budget: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = Utils.getFormattedCurrency(movie.budget),
                                        style = MaterialTheme.typography.subtitle1,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Revenue: ",
                                        style = MaterialTheme.typography.h6
                                    )
                                    Text(
                                        text = Utils.getFormattedCurrency(movie.revenue),
                                        style = MaterialTheme.typography.subtitle1,
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                if (state.error.isNotBlank()) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}