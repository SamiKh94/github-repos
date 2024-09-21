package com.githubrepos.app.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.githubrepos.app.domain.NetworkDataSource
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.ui.repository.RepositoriesPagingSource
import com.githubrepos.app.utils.Dispatcher
import com.githubrepos.app.utils.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Remote implementation of the [GithubRepositoriesRepository]
 */
class GithubRemoteRepositoriesRepository @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val datasource: NetworkDataSource,
) : GithubRepositoriesRepository {

    override fun getPagedRepositories(
        creationPeriod: CreationPeriod,
    ): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,  // Number of items per page
                enablePlaceholders = false  // Disable empty placeholders
            ),
            pagingSourceFactory = { RepositoriesPagingSource(datasource, creationPeriod) }
        ).flow.map {
            it.map { repository ->
                RepositoryItem(
                    id = repository.id,
                    name = repository.name,
                    description = repository.description,
                    stars = repository.stargazersCount,
                    owner = repository.owner
                )
            }
        }
    }

    override fun getRepositories(creationPeriod: CreationPeriod): Flow<List<RepositoryItem>> =
        flow {
            emit(
                datasource.getRepositories(creationPeriod = creationPeriod)?.repositories.orEmpty()
                    .map { repository ->
                        RepositoryItem(
                            id = repository.id,
                            name = repository.name,
                            description = repository.description,
                            stars = repository.stargazersCount,
                            owner = repository.owner
                        )
                    }
            )
        }.flowOn(ioDispatcher)
}
