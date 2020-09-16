package com.example.famouspeople.util

import com.example.famouspeople.peopleList.core.domain.PeopleResult
import com.example.famouspeople.networking.entities.NetworkPeopleResult
import com.example.famouspeople.peopleList.ui.modelClass.ViewPeopleResult

fun NetworkPeopleResult.toPeopleResult(): PeopleResult {
    return PeopleResult(
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )
}


fun PeopleResult.toViewPeopleResult(): ViewPeopleResult {
    return ViewPeopleResult(
        gender = gender,
        id = id,
        knownForDepartment = knownForDepartment,
        name = name,
        popularity = popularity,
        profilePath = profilePath
    )
}