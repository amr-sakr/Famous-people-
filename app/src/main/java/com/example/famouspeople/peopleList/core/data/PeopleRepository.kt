package com.example.famouspeople.peopleList.core.data

import com.example.famouspeople.networking.Result
import com.example.famouspeople.peopleList.core.domain.PeopleResult
import com.example.famouspeople.networking.SafeApiRequest
import com.example.famouspeople.networking.remoteDataSource.WebService
import com.example.famouspeople.util.toPeopleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class PeopleRepository @Inject constructor(private val webServices: WebService) : IPeopleDataSource,
    SafeApiRequest() {

    override suspend fun getPeople(key: String, page: Int): Result<List<PeopleResult>?> {
        val result = apiRequest {
            webServices.getPopularPeopleList(key, page)
        }

        return when (result) {
            is Result.Success -> {
                val data = result.data.networkResults?.map {
                    it.toPeopleResult()
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