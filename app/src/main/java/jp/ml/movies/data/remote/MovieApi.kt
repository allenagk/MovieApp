package jp.ml.movies.data.remote

import jp.ml.movies.common.Constants
import jp.ml.movies.data.remote.dto.MovieDetailDto
import jp.ml.movies.data.remote.dto.MovieSearchResultDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int = 1,
    ): MovieSearchResultDto

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
        @Query("query") query: String,
    ): MovieSearchResultDto

    @GET("movie/{movieId}")
    suspend fun getMovieById(
        @Path("movieId") movieId: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ): MovieDetailDto

}