package jp.ml.movies.data.repository

import jp.ml.movies.data.local.FavoriteDao
import jp.ml.movies.data.local.entity.Favorite
import jp.ml.movies.data.remote.MovieApi
import jp.ml.movies.data.remote.dto.MovieDetailDto
import jp.ml.movies.data.remote.dto.MovieDto
import jp.ml.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Movie repository implementation to access movies from remote and local
 */
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: FavoriteDao
) : MovieRepository {

    override suspend fun getPopularMovies(): List<MovieDto> {
        return api.getPopularMovies().results
    }

    override suspend fun searchMovie(query: String): List<MovieDto> {
        return api.searchMovie(query = query, page = 1).results
    }

    override suspend fun getMovieById(movieId: String): MovieDetailDto {
        return api.getMovieById(movieId = movieId)
    }

    override fun getFavorites(): Flow<List<Favorite>> {
        return dao.getFavorites()
    }

    override fun getFavoriteById(id: Int): Flow<List<Favorite>> {
        return dao.getFavoriteById(id)
    }

    override suspend fun insertFavorite(favorite: Favorite) {
        dao.insertFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: Favorite) {
        dao.deleteFavorite(favorite)
    }

    override suspend fun deleteFavoriteById(id: Int) {
        dao.deleteFavoriteById(id)
    }
}