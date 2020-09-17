package com.example.famouspeople.features.peopleList.core.data

import com.example.famouspeople.networking.Result
import com.example.famouspeople.features.peopleList.core.domain.Person

interface IPeopleDataSource {

    suspend fun getPeople(key: String, page: Int): Result<List<Person>?>

}