package jp.ml.movies.domain.repository

import jp.ml.movies.data.local.entity.Favorite
import jp.ml.movies.data.remote.dto.MovieDetailDto
import jp.ml.movies.data.remote.dto.MovieDto
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getPopularMovies(): List<MovieDto>

    suspend fun searchMovie(query: String): List<MovieDto>

    suspend fun getMovieById(movieId: String): MovieDetailDto

    fun getFavorites(): Flow<List<Favorite>>

    fun getFavoriteById(id: Int): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    suspend fun deleteFavoriteById(id: Int)
}