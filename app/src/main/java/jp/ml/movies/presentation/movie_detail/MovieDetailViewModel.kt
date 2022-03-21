package jp.ml.movies.presentation.movie_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ml.movies.common.Constants
import jp.ml.movies.common.Resource
import jp.ml.movies.domain.model.MovieDetail
import jp.ml.movies.domain.use_case.get_favorites.GetFavoritesUseCase
import jp.ml.movies.domain.use_case.get_movie.GetMovieUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieUseCase: GetMovieUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(MovieDetailState())
    val state: State<MovieDetailState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _isSavedState = mutableStateOf(false)
    val isSavedState: State<Boolean> = _isSavedState

    init {
        //Read movie from API if the screen transition is not from FavoriteScreen
        //If the screen transition is from Favorites load data from local storage
        val isLocal = savedStateHandle.get<Boolean>(Constants.PARAM_IS_LOCAL) ?: false
        savedStateHandle.get<String>(Constants.PARAM_MOVIE_ID)?.let { movieId ->
            if (!isLocal) {
                getMovie(movieId)
            }
            getSavedState(movieId.toInt(), isLocal)
        }
    }

    /**
     * Get movies from the remote database
     */
    private fun getMovie(movieId: String) {
        getMovieUseCase(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = MovieDetailState(movie = result.data)
                }
                is Resource.Error -> {
                    _state.value = MovieDetailState(
                        error = result.message ?: "An unexpected error occurred"
                    )
                }
                is Resource.Loading -> {
                    _state.value = MovieDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Save or delete the favorite movie according to the isSaved flag
     */
    fun saveOrDeleteMovie(movie: MovieDetail, isSaved: Boolean) {
        viewModelScope.launch {
            try {
                if (!isSaved) {
                    //Save the movie to local database
                    getFavoritesUseCase.invoke(movie.toFavorite())
                    //Read favorite icon flag value
                    getSavedState(movie.movieId, false)
                    //Emit message to display in the snackbar
                    _eventFlow.emit(UiEvent.SaveFavorite("Saved to Favorites"))
                } else {
                    //Delete the movie from local database
                    getFavoritesUseCase.deleteFavorite(movie.movieId)
                    //Read favorite icon flag value
                    getSavedState(movie.movieId, false)
                    //Emit message to display in the snackbar
                    _eventFlow.emit(UiEvent.SaveFavorite("Removed From Favorites"))
                }
            } catch (e: Exception) {
                _eventFlow.emit(
                    UiEvent.ShowSnackbar(
                        message = e.message ?: "Couldn't save note"
                    )
                )
            }
        }
    }

    /**
     * This method sets the favorite icon variable
     * Further if data needs to be serve from local database emit local data too
     */
    private fun getSavedState(movieId: Int, displayLocalData: Boolean) {
        getFavoritesUseCase(movieId).onEach { result ->
            _isSavedState.value = result.isNotEmpty()
            if (displayLocalData) {
                if (result.isNotEmpty()) {
                    _state.value = MovieDetailState(movie = result[0].toMovieDetail())
                }
            }
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class SaveFavorite(val message: String) : UiEvent()
    }
}