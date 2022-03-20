package jp.ml.movies.data.repository

import jp.ml.movies.data.remote.MovieApi
import jp.ml.movies.data.remote.dto.MovieDetailDto
import jp.ml.movies.data.remote.dto.MovieDto
import jp.ml.movies.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository {

    override suspend fun getMovies(): List<MovieDto> {
        return api.getMovies(query = "a", page = 1).results
    }

    override suspend fun getMovieById(movieId: String): MovieDetailDto {
        return api.getMovieById(movieId = movieId)
    }
}