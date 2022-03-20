package jp.ml.movies.domain.use_case.get_movies

import jp.ml.movies.common.Resource
import jp.ml.movies.data.remote.dto.toMovie
import jp.ml.movies.domain.model.Movie
import jp.ml.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {

    operator fun invoke(query: String): Flow<Resource<List<Movie>>> = flow {
        try {
            emit(Resource.Loading<List<Movie>>())
            val movies = repository.searchMovie(query).map { it.toMovie() }
            emit(Resource.Success<List<Movie>>(movies))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Movie>>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<List<Movie>>("Couldn't reach server. Check your internet connection."))
        }
    }
}