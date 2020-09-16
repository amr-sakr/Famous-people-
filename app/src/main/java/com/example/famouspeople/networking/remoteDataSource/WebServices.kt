package com.example.famouspeople.networking.remoteDataSource

import com.example.famouspeople.BuildConfig
import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.entities.PeopleResponse
import com.example.famouspeople.util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface WebService {

    @GET("person/popular")
    suspend fun getPopularPeopleList(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Response<PeopleResponse>

    //TODO remove after implementing DI
    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor,
            loggingInterceptor: HttpLoggingInterceptor

        ): WebService {

            loggingInterceptor.apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val okHttpClint = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(loggingInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okHttpClint)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }
}