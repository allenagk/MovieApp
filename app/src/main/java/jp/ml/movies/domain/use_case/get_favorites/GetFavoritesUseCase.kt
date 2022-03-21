package jp.ml.movies.domain.use_case.get_favorites

import jp.ml.movies.data.local.entity.Favorite
import jp.ml.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    fun getFavorites(): Flow<List<Favorite>> {
        return repository.getFavorites()
    }

    suspend operator fun invoke(favorite: Favorite) {
        repository.insertFavorite(favorite)
    }

    operator fun invoke(movieId: Int): Flow<List<Favorite>> {
        return repository.getFavoriteById(movieId)
    }

    suspend fun deleteFavorite(id: Int) {
        repository.deleteFavoriteById(id)
    }
}