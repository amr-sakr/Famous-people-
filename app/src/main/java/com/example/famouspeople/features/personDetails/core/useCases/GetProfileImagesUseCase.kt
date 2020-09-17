package com.example.famouspeople.features.personDetails.core.useCases

import com.example.famouspeople.features.personDetails.core.data.IPersonProfileImagesDataSource

class GetProfileImagesUseCase(private val dataSource: IPersonProfileImagesDataSource) {

    suspend fun invoke(personId: Int, apiKey: String) =
        dataSource.getPersonProfileImages(personId, apiKey)
}