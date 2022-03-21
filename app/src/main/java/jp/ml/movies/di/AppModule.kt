package jp.ml.movies.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.ml.movies.common.Constants
import jp.ml.movies.data.local.MovieDatabase
import jp.ml.movies.data.remote.MovieApi
import jp.ml.movies.data.repository.MovieRepositoryImpl
import jp.ml.movies.domain.repository.MovieRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides MovieApi instance
     */
    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApi::class.java)
    }

    /**
     * Provides MoviesDatabase instance
     */
    @Provides
    @Singleton
    fun provideMoviesDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).build()
    }

    /**
     * Provides MovieRepository instance
     */
    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi, db: MovieDatabase): MovieRepository {
        return MovieRepositoryImpl(api, db.favoriteDao)
    }

}