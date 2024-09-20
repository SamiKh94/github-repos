package com.githubrepos.app.domain

import androidx.core.os.trace
import com.githubrepos.app.BuildConfig
import com.githubrepos.app.domain.models.CreationPeriod
import com.githubrepos.app.domain.models.RepositoriesResponse
import com.githubrepos.app.utils.Dispatcher
import com.githubrepos.app.utils.Dispatchers
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.Serializable
import okhttp3.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNetworkApi {
    @GET(value = "repositories?q=created&sort=stars&order=desc")
    suspend fun getRepositories(
        @Query("date") date: CreationPeriod,
    ): RepositoriesResponse

}

private const val BASE_URL = BuildConfig.GITHUB_URL

/**
 * Wrapper for data provided from the [BASE_URL]
 */
@Serializable
data class NetworkResponse<T>(
    val data: T?,
)

/**
 * [Retrofit] backed [NetworkDataSource]
 */
@Singleton
internal class RetrofitNetwork @Inject constructor(
    @Dispatcher(Dispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    gson: Gson,
    okhttpCallFactory: dagger.Lazy<Call.Factory>,
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

    override suspend fun getRepositories(creationPeriod: CreationPeriod): RepositoriesResponse? =
        networkApi.getRepositories(date = creationPeriod)
}
