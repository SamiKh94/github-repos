package com.githubrepos.app.ui.repository

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(
    private val githubRemoteRepositoriesRepository: GithubRemoteRepositoriesRepository,
    private val githubFavoriteRepositoriesOfflineRepository: GithubFavoriteRepositoriesOfflineRepository
) : ViewModel() {

    val itemSelectionStateFlow = MutableStateFlow<RepositoryItem?>(null)

    private val _creationPeriodMutableStateFlow = MutableStateFlow(CreationPeriod.A_MONTH)
    private val _pagedRepositoriesStateFlow = _creationPeriodMutableStateFlow.flatMapLatest {
        getPagedRepositories(
            it
        )
    }
        .mutableStateIn(viewModelScope, initialValue = PagingData.empty())

    private val _favRepositoriesStateFlow =
        githubFavoriteRepositoriesOfflineRepository.getFavGithubRepositories()
            .mutableStateIn(viewModelScope, initialValue = emptyList())

    val repositoriesResultUIState: StateFlow<RepositoryUiState> = combine(
        _pagedRepositoriesStateFlow,
        _favRepositoriesStateFlow
    ) { repositories, favRepositories ->
        val favRepositoriesIds =
            favRepositories.map { it.id }
        repositories.map { repositoryItem ->
            repositoryItem.copy(isFavorite = favRepositoriesIds.contains(repositoryItem.id))
        }

    }.asResult()
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
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepositoryUiState.Loading,
        )

    fun updateCreationPeriodSelection(value: CreationPeriod) {
        if (value == _creationPeriodMutableStateFlow.value) return
        _creationPeriodMutableStateFlow.value = value
        refreshData()
    }

    private fun getPagedRepositories(
        creationPeriod: CreationPeriod,
    ): Flow<PagingData<RepositoryItem>> {
        return githubRemoteRepositoriesRepository.getPagedRepositories(creationPeriod = creationPeriod)
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

    private fun refreshData() {
        viewModelScope.launch {
            githubRemoteRepositoriesRepository.getPagedRepositories(creationPeriod = _creationPeriodMutableStateFlow.value)
        }
    }
}

sealed interface RepositoryUiState {
    data class Success(val repositories: PagingData<RepositoryItem>) : RepositoryUiState
    data object Error : RepositoryUiState
    data object Loading : RepositoryUiState
}

fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T> {
    val flow = MutableStateFlow(initialValue)

    scope.launch {
        this@mutableStateIn.collect(flow)
    }

    return flow
}