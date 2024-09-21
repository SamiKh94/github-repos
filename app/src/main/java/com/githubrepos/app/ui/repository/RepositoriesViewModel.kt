package com.githubrepos.app.ui.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.githubrepos.app.data.remote.GithubRemoteRepositoriesRepository
import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.utils.Result
import com.githubrepos.app.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RepositoriesViewModel @Inject constructor(private val githubRemoteRepositoriesRepository: GithubRemoteRepositoriesRepository) :
    ViewModel() {

    private val _creationPeriodMutableStateFlow = MutableStateFlow(CreationPeriod.A_MONTH)

    @OptIn(ExperimentalCoroutinesApi::class)
    val repositoriesUiState: StateFlow<RepositoryUiState> =
        _creationPeriodMutableStateFlow.flatMapLatest {
            repositoriesUiState()
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepositoryUiState.Loading,
        )

    private fun repositoriesUiState(
    ): Flow<RepositoryUiState> {

        // Observe topic information
        val topicStream: Flow<PagingData<RepositoryItem>> = getPagedRepositories()

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

    fun updateCreationPeriodSelection(value: CreationPeriod) {
        if (value == _creationPeriodMutableStateFlow.value) return
        _creationPeriodMutableStateFlow.value = value
    }

    private fun getPagedRepositories(): Flow<PagingData<RepositoryItem>> {
        return githubRemoteRepositoriesRepository.getPagedRepositories(creationPeriod = _creationPeriodMutableStateFlow.value)
            .cachedIn(viewModelScope)
    }
}

sealed interface RepositoryUiState {
    data class Success(val repositories: PagingData<RepositoryItem>) : RepositoryUiState
    data object Error : RepositoryUiState
    data object Loading : RepositoryUiState
}