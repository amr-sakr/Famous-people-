package com.example.famouspeople.features.personDetails.core.data

import com.example.famouspeople.features.personDetails.core.domain.Profile
import com.example.famouspeople.networking.Result
import com.example.famouspeople.networking.SafeApiRequest
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.util.toProfile
import javax.inject.Inject

class PersonProfileRepository @Inject constructor(private val webService: WebService) :
    IPersonProfileImagesDataSource,
    SafeApiRequest() {
    override suspend fun getPersonProfileImages(
        personId: Int,
        apiKey: String
    ): Result<List<Profile>?> {
        val result = apiRequest {
            webService.getPersonProfileImages(personId, apiKey)
        }

        return when (result) {
            is Result.Success -> {
                val data = result.data.profiles?.mapNotNull {
                    it.toProfile()
                }
                Result.Success(data)
            }

            is Result.Error -> result

            else -> {
                Result.Error(Exception())
            }
        }
    }
}