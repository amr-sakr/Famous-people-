package com.example.famouspeople.networking.remoteDataSource

import com.example.famouspeople.networking.entities.peopleList.PeopleResponse
import com.example.famouspeople.networking.entities.profileImages.ProfileImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WebService {

    @GET("person/popular")
    suspend fun getPopularPeopleList(
        @Query("api_key") key: String,
        @Query("page") page: Int
    ): Response<PeopleResponse>


    @GET("person/{id}/images")
    suspend fun getPersonProfileImages(
        @Path("id") personId: Int,
        @Query("api_key") apiKey: String
    ): Response<ProfileImageResponse>

}