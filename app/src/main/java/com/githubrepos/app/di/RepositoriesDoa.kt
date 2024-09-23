package com.githubrepos.app.di

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.githubrepos.app.data.remote.RepositoryItem
import kotlinx.coroutines.flow.Flow

/**
 * DAO for [RepositoryEntity] access
 */
@Dao
interface FavRepositoriesDao {

    @Query(value = "SELECT * FROM repositories")
    fun getFavRepositoriesEntities(): Flow<List<RepositoryEntity>>

    /**
     * Inserts [repositoryEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreRepositories(topicEntities: List<RepositoryEntity>): List<Long>

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Upsert
    suspend fun upsertRepositories(entities: List<RepositoryEntity>)

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM repositories
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteRepositories(ids: List<Int>)
}
