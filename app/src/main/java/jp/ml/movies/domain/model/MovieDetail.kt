package jp.ml.movies.domain.model

import jp.ml.movies.data.local.entity.Favorite

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
) {
    fun toFavorite(): Favorite {
        return Favorite(
            id = movieId,
            overview = overview,
            posterPath = posterPath,
            backDropPath = backDropPath,
            releaseDate = releaseDate,
            title = title,
            genres = genres,
            runtime = runtime,
            tagline = tagline,
            voteAverage = voteAverage,
            status = status,
            originalLanguage = originalLanguage,
            budget = budget,
            revenue = revenue,
        )
    }
}
