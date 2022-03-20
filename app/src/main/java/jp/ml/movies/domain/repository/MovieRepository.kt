package jp.ml.movies.domain.repository

import jp.ml.movies.data.remote.dto.MovieDetailDto
import jp.ml.movies.data.remote.dto.MovieDto

interface MovieRepository {

    suspend fun getMovies() : List<MovieDto>

    suspend fun getMovieById(movieId: String) : MovieDetailDto
}