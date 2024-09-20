package com.githubrepos.app.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.githubrepos.app.data.remote.GithubRemoteRepositoriesRepository
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.utils.Result
import com.githubrepos.app.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(githubRemoteRepositoriesRepository: GithubRemoteRepositoriesRepository) :
    ViewModel() {

    val repositoriesUiState: StateFlow<RepositoryUiState> =
        repositoriesUiState(githubRemoteRepositoriesRepository)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = RepositoryUiState.Loading,
            )

    private fun repositoriesUiState(
        githubRemoteRepositoriesRepository: GithubRemoteRepositoriesRepository,
    ): Flow<RepositoryUiState> {

        // Observe topic information
        val topicStream: Flow<List<RepositoryItem>> =
            githubRemoteRepositoriesRepository.getRepositories(
                creationPeriod = CreationPeriod.A_MONTH
            )

        return topicStream
            .asResult()
            .map { repositoryItem ->
                when (repositoryItem) {
                    is Result.Success -> {
                        val repositories = repositoryItem.data
                        RepositoryUiState.Success(
                            repositories = repositories
                        )
                    }

                    is Result.Loading -> RepositoryUiState.Loading
                    is Result.Error -> RepositoryUiState.Error
                }
            }
    }
}

sealed interface RepositoryUiState {
    data class Success(val repositories: List<RepositoryItem>) : RepositoryUiState
    data object Error : RepositoryUiState
    data object Loading : RepositoryUiState
}