package com.example.famouspeople.networking.entities


import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val networkResults: List<NetworkPeopleResult>? = null,
    @SerializedName("total_pages")
    val totalPages: Int? = null,
    @SerializedName("total_results")
    val totalResults: Int? = null
)