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
        savedStateHandle.get<String>(Constants.PARAM_MOVIE_ID)?.let { movieId ->
            getMovie(movieId)
            getSavedState(movieId.toInt())
        }
    }

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

    fun saveOrDeleteMovie(movie: MovieDetail, isSaved: Boolean) {
        viewModelScope.launch {
            try {
                if (!isSaved) {
                    getFavoritesUseCase.invoke(movie.toFavorite())
                    getSavedState(movie.movieId)
                    _eventFlow.emit(UiEvent.SaveFavorite("Saved to Favorites"))
                } else {
                    getFavoritesUseCase.deleteFavorite(movie.movieId)
                    getSavedState(movie.movieId)
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

    private fun getSavedState(movieId: Int) {
        getFavoritesUseCase(movieId).onEach { result ->
            _isSavedState.value = result.isNotEmpty()
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class SaveFavorite(val message: String) : UiEvent()
    }
}