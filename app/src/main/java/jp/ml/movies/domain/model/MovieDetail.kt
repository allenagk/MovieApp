package jp.ml.movies.domain.model

data class MovieDetail(
    val movieId: Int,
    val overview: String,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
)
