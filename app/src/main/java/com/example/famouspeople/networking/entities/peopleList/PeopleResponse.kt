package com.example.famouspeople.networking.entities.peopleList


import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val networkResults: List<NetworkPerson>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)