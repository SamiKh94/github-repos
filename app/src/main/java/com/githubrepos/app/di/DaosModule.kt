package com.githubrepos.app.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesRepositoriesDao(
        database: FavGithubReposDatabase,
    ): FavRepositoriesDao = database.repositoriesDao()

}