package com.githubrepos.app.data.offline

import com.githubrepos.app.data.remote.RepositoryItem
import kotlinx.coroutines.flow.Flow

interface GithubFavoriteRepositoriesOfflineRepository {

    suspend fun getFavGithubRepositories(): Flow<List<RepositoryItem>>

    suspend fun insertFavGithubRepository(repositoryItem: RepositoryItem)

    fun observeAllFavouriteRepositories(): Flow<List<RepositoryItem>>
}