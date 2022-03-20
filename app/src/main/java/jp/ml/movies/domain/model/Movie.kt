package jp.ml.movies.domain.model


data class Movie(
    val id: Int,
    val overview: String,
    val posterPath: String?,
    val title: String,
)
