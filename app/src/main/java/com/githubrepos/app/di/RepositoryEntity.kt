package com.githubrepos.app.di

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.githubrepos.app.data.remote.RepositoryItem


@Entity(
    tableName = "repositories",
)
data class RepositoryEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String?,
    val language: String?,
    val forks: Int,
    val ownerLoginName: String,
    val ownerAvatarUrl: String?,
    val createdAt: String,
    val stars: Int,
    val githubLink: String,
)

fun RepositoryEntity.asExternalModel() = RepositoryItem(
    id = id,
    name = name,
    forks = forks,
    description = description,
    language = language,
    stars = stars,
    githubLink = githubLink,
    createdAt = createdAt,
    ownerLoginName = ownerLoginName,
    ownerAvatarUrl = ownerAvatarUrl,
)

