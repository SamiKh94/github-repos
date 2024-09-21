
package com.githubrepos.app.data.remote

import androidx.paging.PagingData
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.domain.models.Repository
import kotlinx.coroutines.flow.Flow

interface GithubRepositoriesRepository {
    /**
     * Gets the available topics as a stream
     */
    fun getRepositories(creationPeriod: CreationPeriod): Flow<List<RepositoryItem>>

    fun getPagedRepositories(
        creationPeriod: CreationPeriod,
    ): Flow<PagingData<RepositoryItem>>
}
