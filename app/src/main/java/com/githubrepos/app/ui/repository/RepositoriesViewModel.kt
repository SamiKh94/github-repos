package com.githubrepos.app.ui.repository

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.githubrepos.app.data.offline.GithubFavoriteRepositoriesOfflineRepository
import com.githubrepos.app.data.remote.GithubRemoteRepositoriesRepository
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.utils.Result
import com.githubrepos.app.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val githubRemoteRepositoriesRepository: GithubRemoteRepositoriesRepository,
    private val githubFavoriteRepositoriesOfflineRepository: GithubFavoriteRepositoriesOfflineRepository
) : ViewModel() {

    private val _creationPeriodMutableStateFlow = MutableStateFlow(CreationPeriod.A_MONTH)
    private val _searchQueryMutableStateFlow = MutableStateFlow("")

    private val repositoriesStateFlow: StateFlow<PagingData<RepositoryItem>> =
        getPagedRepositories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PagingData.empty(),
        )

    private val favRepositoriesStateFlow: StateFlow<List<RepositoryItem>> =
        githubFavoriteRepositoriesOfflineRepository.getFavGithubRepositories().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    val repositoriesResultUIState: StateFlow<RepositoryUiState> = repositoriesUiState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = RepositoryUiState.Loading,
    )

    private fun repositoriesUiState(
    ): Flow<RepositoryUiState> {

        // Observe repositories information
        val repositoriesStream: Flow<PagingData<RepositoryItem>> =
            combine(
                repositoriesStateFlow,
                favRepositoriesStateFlow,
            ) { repositories, favRepositories ->
                val favRepositoriesIds =
                    favRepositories.map { it.id }
                repositories.map { repositoryItem ->
                    repositoryItem.copy(isFavorite = favRepositoriesIds.contains(repositoryItem.id))
                }
//                    .filter { item ->
//                    item.name.contains(query) || item.ownerLoginName.contains(query)
//                }
            }
        return repositoriesStream
            .asResult()
            .map { repositoryItem ->

                when (repositoryItem) {
                    is Result.Loading -> RepositoryUiState.Loading
                    is Result.Success -> {
                        RepositoryUiState.Success(
                            repositories = repositoryItem.data
                        )
                    }

                    is Result.Error -> RepositoryUiState.Error
                }
            }
    }

    fun updateCreationPeriodSelection(value: CreationPeriod) {
        if (value == _creationPeriodMutableStateFlow.value) return
        _creationPeriodMutableStateFlow.value = value

    }

    private fun getPagedRepositories(): Flow<PagingData<RepositoryItem>> {
        return githubRemoteRepositoriesRepository.getPagedRepositories(creationPeriod = _creationPeriodMutableStateFlow.value)
            .cachedIn(viewModelScope)
    }

    fun markRepositoryAsFavorite(repositoryItem: RepositoryItem) {
        viewModelScope.launch {
            if (repositoryItem.isFavorite) {
                githubFavoriteRepositoriesOfflineRepository.deleteFavGithubRepository(
                    repositoryItem = repositoryItem
                )
            } else {
                githubFavoriteRepositoriesOfflineRepository.insertFavGithubRepository(
                    repositoryItem = repositoryItem
                )
                _creationPeriodMutableStateFlow.value =
                    _creationPeriodMutableStateFlow.value
            }
        }
    }

    fun performSearch(query: String) {
        _searchQueryMutableStateFlow.value = query
    }
}

sealed interface RepositoryUiState {
    data class Success(val repositories: PagingData<RepositoryItem>) : RepositoryUiState
    data object Error : RepositoryUiState
    data object Loading : RepositoryUiState
}