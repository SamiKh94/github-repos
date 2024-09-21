package com.githubrepos.app.domain

import android.util.Log
import com.githubrepos.app.utils.RepositoriesPagingSingleton
import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val linkHeader = response.header("Link")
        // <https://api.github.com/search/repositories?q=created&sort=stars&order=desc&per_page=100&date=A_MONTH&page=2>; rel="next", <https://api.github.com/search/repositories?q=created&sort=stars&order=desc&per_page=100&date=A_MONTH&page=10>; rel="last"
        val pagingUrls = linkHeader?.split(",")
        pagingUrls?.forEach {
            if (it.contains("next")) {
                RepositoriesPagingSingleton.nextPagingLink =
                    it.replace(">", "").replace("<", "").split(";")[0]
            }

            if (it.contains("last")) {
                RepositoriesPagingSingleton.lastPagingLink =
                    it.replace(">", "").replace("<", "").split(";")[0]

            }
        }
        return response
    }
}
