package com.githubrepos.app.domain.models


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class License(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("node_id")
    val nodeId: String,
    @SerializedName("spdx_id")
    val spdxId: String,
    @SerializedName("url")
    val url: String
)