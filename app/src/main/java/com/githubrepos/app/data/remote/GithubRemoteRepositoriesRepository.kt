package com.githubrepos.app.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.githubrepos.app.domain.NetworkDataSource
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.ui.repository.RepositoriesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Remote implementation of the [GithubRepositoriesRepository]
 */
class GithubRemoteRepositoriesRepository @Inject constructor(
    private val datasource: NetworkDataSource,
) : GithubRepositoriesRepository {

    override fun getPagedRepositories(
        creationPeriod: CreationPeriod,
    ): Flow<PagingData<RepositoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,  // Number of items per page
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
                    ownerLoginName = repository.owner.login,
                    ownerAvatarUrl = repository.owner.avatarUrl,
                    forks = repository.forks,
                    githubLink = repository.htmlUrl,
                    language = repository.language,
                    createdAt = repository.createdAt
                )
            }
        }
    }
}
