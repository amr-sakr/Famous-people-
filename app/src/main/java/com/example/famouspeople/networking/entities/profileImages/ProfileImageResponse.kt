package com.example.famouspeople.networking.entities.profileImages


import com.google.gson.annotations.SerializedName

data class ProfileImageResponse(
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("profiles")
    val profiles: List<NetworkProfile>? = null
)