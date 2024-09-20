package com.githubrepos.app.data.remote

import com.githubrepos.app.domain.models.Owner

data class RepositoryItem(
    val id: Int,
    val name: String,
    val owner: Owner,
    val stars: Int
)
