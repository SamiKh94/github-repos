package com.githubrepos.app.di

import com.githubrepos.app.data.offline.GithubFavoriteRepositoriesOfflineRepository
import com.githubrepos.app.data.offline.GithubFavoriteRepositoriesOfflineRepositoryImpl
import com.githubrepos.app.data.remote.GithubRemoteRepositoriesRepository
import com.githubrepos.app.data.remote.GithubRepositoriesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsRepositoriesRepository(
        repositoriesRepository: GithubRemoteRepositoriesRepository,
    ): GithubRepositoriesRepository

    @Binds
    internal abstract fun bindsFavGithubRepositoriesRepository(
        repositoriesRepository: GithubFavoriteRepositoriesOfflineRepositoryImpl,
    ): GithubFavoriteRepositoriesOfflineRepository
}
