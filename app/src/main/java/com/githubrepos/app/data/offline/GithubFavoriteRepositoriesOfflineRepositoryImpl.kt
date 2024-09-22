package com.githubrepos.app.data.offline

import com.githubrepos.app.data.remote.RepositoryItem
import com.githubrepos.app.data.remote.asEntity
import com.githubrepos.app.di.FavRepositoriesDao
import com.githubrepos.app.di.asExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GithubFavoriteRepositoriesOfflineRepositoryImpl @Inject constructor(private val repositoriesDao: FavRepositoriesDao) :
    GithubFavoriteRepositoriesOfflineRepository {
    override suspend fun getFavGithubRepositories(): Flow<List<RepositoryItem>> {
        return repositoriesDao.getFavRepositoriesEntities()
            .map { it.map { entity -> entity.asExternalModel() } }
    }

    override suspend fun insertFavGithubRepository(repositoryItem: RepositoryItem) {
        repositoriesDao.upsertRepositories(listOf(repositoryItem.asEntity()))
    }

    override fun observeAllFavouriteRepositories(): Flow<List<RepositoryItem>> {
        return repositoriesDao.getFavRepositoriesEntities().distinctUntilChanged()
            .map { it.map { entity -> entity.asExternalModel() } }
    }
}