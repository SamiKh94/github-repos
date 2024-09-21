package com.githubrepos.app.ui.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.githubrepos.app.domain.NetworkDataSource
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.domain.models.Repository

class RepositoriesPagingSource(
    private val datasource: NetworkDataSource,
    private val creationPeriod: CreationPeriod,
) : PagingSource<Int, Repository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        return try {
            val currentPage = params.key ?: 1
            val response =
                datasource.getPagedRepositories(creationPeriod = creationPeriod, page = currentPage)
            val data = response?.repositories ?: emptyList()

            // Determine the next and previous page
            val nextPage = if (data.isEmpty()) null else currentPage + 1
            val prevPage = if (currentPage == 1) null else currentPage - 1

            LoadResult.Page(
                data = data,
                prevKey = prevPage,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}
