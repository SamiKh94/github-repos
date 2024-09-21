package com.githubrepos.app.domain.models


enum class CreationPeriod(val query: String) {
    A_DAY("created:>\$(date -d '1 day ago' '+YYYY-mm-dd')"),

    A_WEEK("created:>\$(date -d '1 week ago' '+YYYY-mm-dd')"),

    A_MONTH("created:>\$(date -d '1 month ago' '+YYYY-mm-dd')"),
}