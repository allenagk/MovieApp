package jp.ml.movies.presentation.movie_detail

import jp.ml.movies.domain.model.MovieDetail

data class MovieDetailState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val error: String = ""
)
