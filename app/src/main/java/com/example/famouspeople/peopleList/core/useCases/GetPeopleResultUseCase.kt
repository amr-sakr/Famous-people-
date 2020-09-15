package com.example.famouspeople.peopleList.core.useCases

import com.example.famouspeople.peopleList.core.data.IPeopleDataSource
import javax.inject.Inject

class GetPeopleResultUseCase @Inject constructor(private val dataSource: IPeopleDataSource) {
    suspend operator fun invoke(key: String, page: Int) = dataSource.getPeople(key, page)
}