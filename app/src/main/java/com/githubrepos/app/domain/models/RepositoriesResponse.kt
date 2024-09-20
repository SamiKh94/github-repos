package com.githubrepos.app.domain.models


import com.google.gson.annotations.SerializedName

data class RepositoriesResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean,
    @SerializedName("items")
    val repositories: List<Repository>,
    @SerializedName("total_count")
    val totalCount: Int
)