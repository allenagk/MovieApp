package jp.ml.movies.domain.model

data class MovieDetail(
    val movieId: Int,
    val overview: String?,
    val posterPath: String?,
    val backDropPath: String?,
    val releaseDate: String?,
    val title: String?,
    val genres: List<String>,
    val runtime: Int?,
    val tagline: String?,
    val voteAverage: Double?,
    val status: String?,
    val originalLanguage: String?,
    val budget: Int?,
    val revenue: Int?,
)
