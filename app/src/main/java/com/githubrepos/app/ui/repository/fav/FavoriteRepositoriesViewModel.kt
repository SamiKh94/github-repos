package com.githubrepos.app.ui.repository.fav

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.githubrepos.app.data.offline.GithubFavoriteRepositoriesOfflineRepository
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.ui.repository.RepositoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FavoriteRepositoriesViewModel @Inject constructor(favoriteRepositoriesOfflineRepository: GithubFavoriteRepositoriesOfflineRepository) :
    ViewModel() {

    val favoriteRepositoriesUIState: StateFlow<FavoriteRepositoryUiState> =
        favoriteRepositoriesOfflineRepository.observeAllFavouriteRepositories()
            .map<List<RepositoryItem>, FavoriteRepositoryUiState>(FavoriteRepositoryUiState::Success)
            .onStart { emit(FavoriteRepositoryUiState.Loading) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteRepositoryUiState.Loading,
            )

}


sealed interface FavoriteRepositoryUiState {
    data class Success(val repositories: List<RepositoryItem>) : FavoriteRepositoryUiState
    data object Error : FavoriteRepositoryUiState
    data object Loading : FavoriteRepositoryUiState
}