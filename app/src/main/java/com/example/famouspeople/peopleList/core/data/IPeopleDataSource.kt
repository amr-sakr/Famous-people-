package com.example.famouspeople.peopleList.core.data

import com.example.famouspeople.peopleList.core.domain.PeopleResult

interface IPeopleDataSource {

    suspend fun getPeople(key: String, page: Int): List<PeopleResult>

}