package com.githubrepos.app.utils

import android.net.Uri

object RepositoriesPagingSingleton {
    var nextPagingLink: String? = null
    var lastPagingLink: String? = null

    val nextPage: Int?
        get() = if (extractPageNumberFromUrl(nextPagingLink)!! >= lastPage!!) null else extractPageNumberFromUrl(nextPagingLink)
    private val lastPage: Int?
        get() = extractPageNumberFromUrl(lastPagingLink)

    private fun extractPageNumberFromUrl(url: String?): Int? {
        url?.let {
            val uri = Uri.parse(it)

            val nextPageNumber =
                uri.queryParameterNames.associateWith { uri.getQueryParameters(it) }["page"]?.get(
                    0
                )?.toInt()
            return nextPageNumber
        } ?: return 1
    }
}