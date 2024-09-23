package com.githubrepos.app.domain

import androidx.core.os.trace
import com.githubrepos.app.BuildConfig
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.domain.models.RepositoriesResponse
import com.githubrepos.app.utils.QueryBuilder
import com.google.gson.Gson
import dagger.Lazy
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Network API
 */
private interface RetrofitNetworkApi {
    @GET(value = "repositories?sort=starts&order=desc")
    suspend fun getRepositories(
        @Query("q") queries: String
    ): RepositoriesResponse

    @GET(value = "repositories?sort=stars&order=desc&per_page=100")
    suspend fun getPagedRepositories(
        @Query("q") queries: String,
        @Query("page") page: Int
    ): RepositoriesResponse

}

private const val BASE_URL = BuildConfig.GITHUB_URL

/**
 * [Retrofit] backed [NetworkDataSource]
 */
@Singleton
internal class RetrofitNetwork @Inject constructor(
    gson: Gson,
    okhttpCallFactory: Lazy<Call.Factory>,
) : NetworkDataSource {

    private val networkApi = trace("RetrofitNiaNetwork") {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // We use callFactory lambda here with dagger.Lazy<Call.Factory>
            // to prevent initializing OkHttp on the main thread.
            .callFactory { okhttpCallFactory.get().newCall(it) }
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(RetrofitNetworkApi::class.java)
    }

    override suspend fun getRepositories(
        creationPeriod: CreationPeriod,
    ): RepositoriesResponse =
        networkApi.getRepositories(queries = QueryBuilder(creationPeriod = creationPeriod).build())

    override suspend fun getPagedRepositories(
        creationPeriod: CreationPeriod,
        page: Int
    ): RepositoriesResponse = networkApi.getPagedRepositories(
        queries = QueryBuilder(creationPeriod = creationPeriod).build(),
        page = page
    )
}
