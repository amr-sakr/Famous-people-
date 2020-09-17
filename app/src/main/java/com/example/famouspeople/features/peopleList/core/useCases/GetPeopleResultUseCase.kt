package com.example.famouspeople.features.peopleList.core.useCases

import com.example.famouspeople.features.peopleList.core.data.IPeopleDataSource
import javax.inject.Inject

class GetPeopleResultUseCase @Inject constructor(private val dataSource: IPeopleDataSource) {
    suspend operator fun invoke(key: String, page: Int) = dataSource.getPeople(key, page)
}