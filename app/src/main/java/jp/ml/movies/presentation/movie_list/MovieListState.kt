package jp.ml.movies.presentation.movie_list

import jp.ml.movies.domain.model.Movie

data class MovieListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String = ""
)