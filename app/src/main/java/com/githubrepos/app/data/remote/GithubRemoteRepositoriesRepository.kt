/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.githubrepos.app.data.remote

import com.githubrepos.app.domain.NetworkDataSource
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.utils.Dispatcher
import com.githubrepos.app.utils.Dispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Remote implementation of the [GithubRepositoriesRepository]
 */
class GithubRemoteRepositoriesRepository @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val datasource: NetworkDataSource,
) : GithubRepositoriesRepository {
    override fun getRepositories(creationPeriod: CreationPeriod): Flow<List<RepositoryItem>> =
        flow {
            emit(
                datasource.getRepositories(creationPeriod = creationPeriod)?.repositories.orEmpty()
                    .map { repository ->
                        RepositoryItem(
                            id = repository.id,
                            name = repository.name,
                            stars = repository.stargazersCount,
                            owner = repository.owner
                        )
                    }
            )
        }.flowOn(ioDispatcher)
}
