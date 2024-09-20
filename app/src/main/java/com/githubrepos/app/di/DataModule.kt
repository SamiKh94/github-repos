package com.githubrepos.app.di

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
        topicsRepository: GithubRemoteRepositoriesRepository,
    ): GithubRepositoriesRepository
}
