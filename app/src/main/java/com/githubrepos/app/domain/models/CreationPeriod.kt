package com.githubrepos.app.domain.models

import com.google.gson.annotations.SerializedName

enum class CreationPeriod {
    @SerializedName("1d")
    A_DAY,
    @SerializedName("1w")
    A_WEEK,
    @SerializedName("1m")
    A_MONTH,
}