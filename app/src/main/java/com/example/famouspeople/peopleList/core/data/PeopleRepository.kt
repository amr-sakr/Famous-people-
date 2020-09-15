package com.example.famouspeople.peopleList.core.data

import com.example.famouspeople.peopleList.core.domain.PeopleResult
import com.example.famouspeople.networking.SafeApiRequest
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.util.toPeopleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PeopleRepository @Inject constructor(private val webServices: WebService) : IPeopleDataSource,
    SafeApiRequest() {

    override suspend fun getPeople(key: String, page: Int): List<PeopleResult> {
        val result = apiRequest {
            webServices.getPopularPeopleList(key, page)
        }

        return withContext(Dispatchers.IO) {
            result.networkResults?.map {
                it.toPeopleResult()
            }
        }!!
    }


}