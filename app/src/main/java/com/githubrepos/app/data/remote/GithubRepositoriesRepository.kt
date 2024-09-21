
package com.githubrepos.app.data.remote

import androidx.paging.PagingData
import com.githubrepos.app.domain.models.CreationPeriod
import kotlinx.coroutines.flow.Flow

interface GithubRepositoriesRepository {

    fun getPagedRepositories(
        creationPeriod: CreationPeriod,
    ): Flow<PagingData<RepositoryItem>>
}
