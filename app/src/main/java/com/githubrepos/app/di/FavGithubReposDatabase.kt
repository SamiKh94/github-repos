package com.githubrepos.app.di

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RepositoryEntity::class], version = 1)
internal abstract class FavGithubReposDatabase : RoomDatabase() {
    abstract fun repositoriesDao(): FavRepositoriesDao
}
