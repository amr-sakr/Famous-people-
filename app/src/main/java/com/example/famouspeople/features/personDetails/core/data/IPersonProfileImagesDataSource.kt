package com.example.famouspeople.features.personDetails.core.data

import com.example.famouspeople.features.personDetails.core.domain.Profile
import com.example.famouspeople.networking.Result

interface IPersonProfileImagesDataSource {

    suspend fun getPersonProfileImages(personId: Int, apiKey: String): Result<List<Profile>?>
}