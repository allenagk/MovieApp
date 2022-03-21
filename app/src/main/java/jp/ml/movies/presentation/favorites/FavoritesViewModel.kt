package jp.ml.movies.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ml.movies.data.local.entity.Favorite
import jp.ml.movies.domain.use_case.get_favorites.GetFavoritesUseCase
import jp.ml.movies.domain.use_case.get_movies.GetMoviesUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    private val _state = mutableListOf<Favorite>()
    val state: List<Favorite> = _state

    init {
        getFavorites()
    }

    /**
     * Get favorites from local database
     */
    private fun getFavorites() {
        getFavoritesUseCase.getFavorites().onEach { result ->
            _state.removeAll { true }
            _state.addAll(result)
        }.launchIn(viewModelScope)
    }

}