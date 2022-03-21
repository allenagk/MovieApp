package jp.ml.movies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jp.ml.movies.domain.model.Movie
import jp.ml.movies.domain.model.MovieDetail

@Entity
data class Favorite(
    @PrimaryKey val id: Int,
    val overview: String?,
    val posterPath: String?,
    val backDropPath: String?,
    val releaseDate: String?,
    val title: String?,
    val genres: String,
    val runtime: Int?,
    val tagline: String?,
    val voteAverage: Double?,
    val status: String?,
    val originalLanguage: String?,
    val budget: Int?,
    val revenue: Int?,
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            overview = overview,
            posterPath = posterPath,
            title = title,
            releaseDate = releaseDate
        )
    }

    fun toMovieDetail(): MovieDetail {
        return MovieDetail(
            movieId = id,
            overview = overview,
            posterPath = posterPath,
            backDropPath = backDropPath,
            releaseDate = releaseDate,
            title = title,
            genres = emptyList(),
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