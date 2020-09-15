package com.example.famouspeople.networking.remoteDataSource

import com.example.famouspeople.networking.NetworkConnectionInterceptor
import com.example.famouspeople.networking.entities.PeopleResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val BASE_URL = "https://api.themoviedb.org/3/"

interface WebService {

    @GET("person/popular")
    suspend fun getPopularPeopleList(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Response<PeopleResponse>

    //TODO remove after implementing DI
    companion object {
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor

        ): WebService {

            val okHttpClint = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
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